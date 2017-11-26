import { Component, OnInit } from '@angular/core';
import { BackendService } from '../../services/backend.service';
import { SessionService } from '../../services/session.service';
import { CryptoService, Keychain, KeyPosition, parseNode } from '../../services/crypto.service';
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
  public selectedNetwork: Network = null;
  public selectedPosition: KeyPosition = null;
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
          keychain.decryptedKey = parseNode(this.cryptoService.decrypt(this.sessionService.getKey(), keychain.key));
          keychain.positions.forEach((position) =>
            position.network = NETWORKS.find((n) => n.config.bip44 === position.coinType));
        });
        if (this.keychains.length > 0 && this.selectedChain === null) {
          this.selectedChain = keychains[0];
          if (this.selectedChain.positions && this.selectedChain.positions.length > 0) {
            this.selectPosition(this.selectedChain.positions[0]);
          }
        }
      });
  }

  public openDialog(dialogContent): void {
    this.customIndexForm.get('customIndex').setValue(0);
    const keychain = this.selectedChain;
    this.modalService.open(dialogContent).result.then(() => {
      this.backendService.addCustomPosition(keychain, this.selectedNetwork.config.bip44, this.customIndex.value)
        .subscribe((position) => {
          keychain.positions.push(position);
          this.selectPosition(position);
        });
    }, noop);
  }

  get customIndex() {
    return this.customIndexForm.get('customIndex');
  }

  public newCoin(network: Network): void {
    this.selectedNetwork = network;
    this.newKey();
  }

  public newKey(): void {
    const keychain = this.selectedChain;
    this.backendService.increasePosition(keychain, this.selectedNetwork.config.bip44)
      .subscribe((position) => {
        position.network = NETWORKS.find((n) => n.config.bip44 === position.coinType);
        const existingIndex = _.findIndex(keychain.positions, (pos) => pos.coinType === position.coinType);
        if (existingIndex >= 0) {
          keychain.positions[existingIndex] = position;
        } else {
          keychain.positions.push(position);
        }
        this.selectPosition(position);
      });
  }

  public getSelectableNetworks(): Network[] {
    const sortedNetworks = _.sortBy(NETWORKS, 'label');
    return sortedNetworks.filter((n) => !_.find(this.selectedChain.positions, (pos) => pos.network.config.bip44 === n.config.bip44));
  }

  public selectPosition(position: KeyPosition) {
    if (position) {
      this.calculateKeys(this.selectedChain, position);
      this.selectedPosition = position;
      this.selectedNetwork = this.selectedPosition.network;
    } else {
      this.selectedPosition = null;
    }
  }

  private calculateKeys(keychain: Keychain, position: KeyPosition): void {
    position.keyPairs = [];
    if (!position.custom) {
      for (let i = 0; i <= position.index; i++) {
        position.keyPairs.push(this.keyPair(keychain, position, i));
      }
    } else {
      position.keyPairs.push(this.keyPair(keychain, position, position.index));
    }
  }

  private keyPair(keychain: Keychain, position: KeyPosition, index: number): string {
    const keyPair = keychain.decryptedKey.derivePath(`m/44'/${position.coinType}'/0'/0/${index}`).keyPair;
    keyPair.index = index;
    keyPair.network = position.network.config;
    keyPair.wif = customToWIF(keyPair, keyPair.network);
    keyPair.address = customGetAddress(keyPair, keyPair.network);
    return keyPair;
  }
}
