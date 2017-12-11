import { Component } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CryptoService, generateDeterministicNode, Keychain, parseNode } from '../../services/crypto.service';
import { BackendService } from '../../services/backend.service';
import { Router } from '@angular/router';
import { NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'add-keychain',
  providers: [],
  templateUrl: './add-keychain.component.html',
  styleUrls: ['./add-keychain.component.scss']
})
export class AddKeychainComponent {

  public keychainForm: FormGroup;
  public activeTab: string;
  public notificationKey: string = null;
  public isSuccess: boolean = false;
  public calculatedCoinomiKey: string = null;

  constructor(public sessionService: SessionService, private formBuilder: FormBuilder, private router: Router,
              public cryptoService: CryptoService, private backendService: BackendService) {
    const generatedKey = generateDeterministicNode();
    this.keychainForm = formBuilder.group({
      name: ['', [Validators.required]],
      generatedKey: [generatedKey, []],
      importedKey: ['', [Validators.minLength(8)]],
      coinomiPhrase: ['', [Validators.minLength(8)]],
      coinomiPassphrase: ['', []],
    });
    this.coinomiPhrase.valueChanges.subscribe((data) => this.onCoinomiInputChanged(data));
    this.coinomiPassphrase.valueChanges.subscribe((data) => this.onCoinomiInputChanged(data));
  }

  public newKey(): void {
    this.keychainForm.controls['generatedKey'].setValue(generateDeterministicNode());
  }

  public changeTab($event: NgbTabChangeEvent): void {
    this.activeTab = $event.nextId;
    if (this.activeTab === 'import-bip32') {
      this.importedKey.setValidators([Validators.required, Validators.minLength(8)]);
      this.coinomiPhrase.setValidators([]);
    } else if (this.activeTab === 'import-coinomi') {
      this.coinomiPhrase.setValidators([Validators.required, Validators.minLength(8)]);
      this.importedKey.setValidators([]);
    } else {
      this.importedKey.setValidators([]);
      this.coinomiPhrase.setValidators([]);
    }
    this.importedKey.updateValueAndValidity();
    this.coinomiPhrase.updateValueAndValidity();
  }

  public save(value): void {
    let bip32RootKey: string = null;
    if (this.activeTab === 'import-bip32') {
      bip32RootKey = value.importedKey;
    } else if (this.activeTab === 'import-coinomi') {
      bip32RootKey = this.calculatedCoinomiKey;
    } else {
      bip32RootKey = value.generatedKey;
    }

    // test key
    try {
      parseNode(bip32RootKey);
    } catch (e) {
      this.notificationKey = 'ch.cloudcoins.errors.invalidBip32RootKey';
      this.isSuccess = false;
      return;
    }

    const keychain: Keychain = {
      name: value.name,
      key: this.cryptoService.encrypt(this.sessionService.getKey(), bip32RootKey),
      positions: []
    };
    this.backendService.createKeychain(keychain).subscribe(() => this.router.navigate(['/my-keychains']));
  }

  private onCoinomiInputChanged(data): void {
    this.calculatedCoinomiKey = generateDeterministicNode(this.coinomiPhrase.value, this.coinomiPassphrase.value);
  }

  get name() {
    return this.keychainForm.get('name');
  }

  get generatedKey() {
    return this.keychainForm.get('generatedKey');
  }

  get importedKey() {
    return this.keychainForm.get('importedKey');
  }

  get coinomiPhrase() {
    return this.keychainForm.get('coinomiPhrase');
  }

  get coinomiPassphrase() {
    return this.keychainForm.get('coinomiPassphrase');
  }
}
