import { Component, OnInit } from '@angular/core';
import { BackendService } from '../../services/backend.service';
import { SessionService } from '../../services/session.service';
import {
  CryptoService,
  generateDeterministicNode, hmacSha512,
  Keychain,
  KeyPosition,
  parseNode
} from '../../services/crypto.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

export const COIN_TYPE_BITCOIN = 0;

@Component({
  selector: 'import-keychains',
  providers: [],
  templateUrl: './import-keychains.component.html',
  styleUrls: ['./import-keychains.component.scss']
})
export class ImportKeychainsComponent implements OnInit {

  public keychains: Keychain[];
  public electrumForm: FormGroup;

  constructor(public cryptoService: CryptoService, public sessionService: SessionService,
              private backendService: BackendService, private formBuilder: FormBuilder) {
    this.electrumForm = formBuilder.group({
      name: ['', [Validators.required]],
      electrumseed: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  public ngOnInit(): void {
  }

  public save(value): void {

    const keychain: Keychain = {
      name: value.name,
      key: hmacSha512(value.electrumseed).toString(),
      positions: []
    };
    console.log(keychain);
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
    return this.electrumForm.get('name');
  }

  get masterKey() {
    return this.electrumForm.get('masterKey');
  }
}
