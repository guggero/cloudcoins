import { Component } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CryptoService, generateDeterministicNode, Keychain } from '../../services/crypto.service';
import { BackendService } from '../../services/backend.service';
import { Router } from '@angular/router';

@Component({
  selector: 'add-keychain',
  providers: [],
  templateUrl: './add-keychain.component.html',
  styleUrls: ['./add-keychain.component.scss']
})
export class AddKeychainComponent {

  public keychainForm: FormGroup;

  constructor(public sessionService: SessionService, private formBuilder: FormBuilder, private router: Router,
              public cryptoService: CryptoService, private backendService: BackendService) {
    const generatedKey = generateDeterministicNode();
    this.keychainForm = formBuilder.group({
      name: ['', [Validators.required]],
      masterKey: [generatedKey, [Validators.required, Validators.minLength(8)]]
    });
  }

  public save(value): void {
    const keychain: Keychain = {
      name: value.name,
      key: this.cryptoService.encrypt(this.sessionService.getKey(), value.masterKey),
      positions: []
    };
    this.backendService.createKeychain(keychain).subscribe(() => this.router.navigate(['/my-keychains']));
  }

  get name() {
    return this.keychainForm.get('name');
  }

  get masterKey() {
    return this.keychainForm.get('masterKey');
  }
}
