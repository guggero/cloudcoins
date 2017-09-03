import { Component, OnInit } from '@angular/core';
import { BackendService } from '../../services/backend.service';
import { SessionService } from '../../services/session.service';
import {
  CryptoService,
  generateDeterministicNode,
  Keychain,
  KeyPosition,
  parseNode
} from '../../services/crypto.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

export const COIN_TYPE_BITCOIN = 0;

@Component({
  selector: 'my-keychains',
  providers: [],
  templateUrl: './my-keychains.component.html',
  styleUrls: ['./my-keychains.component.scss']
})
export class MyKeychainsComponent implements OnInit {

  public keychains: Keychain[];
  public keychainForm: FormGroup;

  constructor(public cryptoService: CryptoService, public sessionService: SessionService,
              private backendService: BackendService, private formBuilder: FormBuilder) {
    const generatedKey = generateDeterministicNode();
    this.keychainForm = formBuilder.group({
      name: ['', [Validators.required]],
      masterKey: [generatedKey, [Validators.required, Validators.minLength(8)]]
    });
  }

  public ngOnInit(): void {
    this.backendService.getKeychains()
      .subscribe((keychains) => {
        this.keychains = keychains;
        this.keychains.forEach((keychain) => {
          keychain.decryptedKey = parseNode(this.cryptoService.decrypt(this.sessionService.getKey(), keychain.key));
          keychain.positions.forEach((position) => this.calculateKeys(keychain, position));
        });
      });
  }

  public newKey(keychain: Keychain): void {
    this.backendService.increasePosition(keychain, COIN_TYPE_BITCOIN)
      .subscribe((position) => {
        this.calculateKeys(keychain, position);
        keychain.positions = [position];
      });
  }

  public save(value): void {
    const keychain: Keychain = {
      name: value.name,
      key: this.cryptoService.encrypt(this.sessionService.getKey(), value.masterKey),
      positions: []
    };
    this.backendService.createKeychain(keychain).subscribe(() => this.ngOnInit());
  }

  private calculateKeys(keychain: Keychain, position: KeyPosition): void {
    position.keyPairs = [];
    for (let i = 0; i <= position.index; i++) {
      position.keyPairs.push({
        privKeyWif: this.privateKey(keychain, position, i),
        address: this.address(keychain, position, i),
      });
    }
  }

  private privateKey(keychain: Keychain, position: KeyPosition, index: number): string {
    return keychain.decryptedKey.derivePath(`m/44'/${position.coinType}'/0'/0/${index}`).keyPair.toWIF();
  }

  private address(keychain: Keychain, position: KeyPosition, index: number): string {
    return keychain.decryptedKey.derivePath(`m/44'/${position.coinType}'/0'/0/${index}`).keyPair.getAddress();
  }

  get name() {
    return this.keychainForm.get('name');
  }

  get masterKey() {
    return this.keychainForm.get('masterKey');
  }
}
