import { Component, OnInit } from '@angular/core';
import { BackendService } from '../../services/backend.service';
import { SessionService } from '../../services/session.service';
import { Coin, CryptoService, Keychain, KeyPair, KeyPosition, parseNode } from '../../services/crypto.service';
import { customGetAddress, customToWIF, Network, NETWORKS } from '../../networks';
import _ from 'lodash';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { noop } from 'rxjs/util/noop';

@Component({
  selector: 'my-keychains',
  providers: [],
  templateUrl: './my-keychains.component.html',
  styleUrls: ['./my-keychains.component.scss']
})
export class MyKeychainsComponent implements OnInit {

  public helpShown: boolean = false;
  public keychains: Keychain[];
  public selectedChain: Keychain = null;
  public selectedCoin: Coin = null;
  public customIndexForm: FormGroup;

  constructor(public cryptoService: CryptoService, public sessionService: SessionService,
              private backendService: BackendService, private formBuilder: FormBuilder,
              private modalService: NgbModal) {
    this.customIndexForm = formBuilder.group({
      customIndex: [0, [Validators.required, Validators.max(2147483646)]]
    });
  }

  public ngOnInit(): void {
    this.backendService.getKeychains()
      .subscribe((keychains) => {
        this.keychains = keychains;
        this.keychains.forEach((keychain) => {
          keychain.hdRootNode = parseNode(this.cryptoService.decrypt(this.sessionService.getKey(), keychain.key));
          keychain.hdBip44Node = keychain.hdRootNode.derivePath(`m/44'`);
          keychain.coins = [];
          _(keychain.positions)
            .groupBy('coinType')
            .forEach((value: KeyPosition[], key: string) => {
              const coinType = parseInt(key, 10);
              const coin = this.createCoin(keychain, coinType, value);
              keychain.coins.push(coin);
            });
        });
        if (this.keychains.length > 0 && this.selectedChain === null) {
          this.selectedChain = keychains[0];
          if (this.selectedChain.coins && this.selectedChain.coins.length > 0) {
            this.selectCoin(this.selectedChain.coins[0]);
          }
        }
      });
  }

  public openDialog(dialogContent): void {
    this.customIndexForm.get('customIndex').setValue(0);
    const keychain = this.selectedChain;
    this.modalService.open(dialogContent).result.then(() => {
      this.backendService.addCustomPosition(keychain, this.selectedCoin.network.config.bip44, this.customIndex.value)
        .subscribe((position: KeyPosition) => this.addPosition(position));
    }, noop);
  }

  get customIndex() {
    return this.customIndexForm.get('customIndex');
  }

  public newCoin(network: Network): void {
    const coin = this.createCoin(this.selectedChain, network.config.bip44, []);
    this.selectedChain.coins.push(coin);
    this.selectCoin(coin);
    this.newKey();
  }

  public newKey(): void {
    const keychain = this.selectedChain;
    this.backendService.increasePosition(keychain, this.selectedCoin.network.config.bip44)
      .subscribe((position: KeyPosition) => this.addPosition(position));
  }

  public getSelectableNetworks(): Network[] {
    const sortedNetworks = _.sortBy(NETWORKS, 'label');
    const coins = this.selectedChain.coins;
    return sortedNetworks.filter((n) => !_.find(coins, (coin) => coin.network.config.bip44 === n.config.bip44));
  }

  public selectCoin(coin: Coin) {
    this.selectedCoin = coin;
  }

  private addPosition(position: KeyPosition): void {
    this.selectedCoin.keyPairs.push(this.getKeyPair(this.selectedCoin.hdNode, position.index, this.selectedCoin.network));
    // invoke change detection by creating a new array reference
    this.selectedCoin.keyPairs = this.selectedCoin.keyPairs.slice();
  }

  private createCoin(keychain: Keychain, coinType: number, positions: KeyPosition[]): Coin {
    const hdNode = this.getCoinParentNode(keychain.hdBip44Node, coinType);
    const network = NETWORKS.find((n) => n.config.bip44 === coinType);
    const coin = {coinType, network, hdNode, keyPairs: []};
    positions.forEach((position: KeyPosition) => {
      if (position.custom) {
        coin.keyPairs.push(this.getKeyPair(hdNode, position.index, network));
      } else {
        for (let i = 0; i <= position.index; i++) {
          coin.keyPairs.push(this.getKeyPair(hdNode, i, network));
        }
      }
    });
    return coin;
  }

  private getCoinParentNode(hdRootNode: any, coinType: number): any {
    return hdRootNode.derivePath(`${coinType}'/0'/0`);
  }

  private getKeyPair(hdParentNode: any, index: number, network: Network): KeyPair {
    const childNode = hdParentNode.derivePath(`${index}`);
    childNode.keyPair.network = network.config;
    const keyPair: KeyPair = {
      index,
      wif: customToWIF(childNode.keyPair, network.config),
      address: customGetAddress(childNode.keyPair, network.config),
      balance: 'n/a'
    };
    if (network.config.apiName) {
      this.backendService.loadBalance(network.config.apiName, keyPair.address)
        .subscribe((response) => {
          if (response && response.addresses) {
            if (response.addresses.length > 0) {
              keyPair.balance = `${response.addresses[0].final_balance / 100000000}`;
            } else if (response.addresses.length === 0) {
              keyPair.balance = '0';
            }
          }
        });
    }
    return keyPair;
  }
}
