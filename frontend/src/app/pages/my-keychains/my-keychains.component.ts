import { Component, OnInit } from '@angular/core';
import { BackendService } from '../../services/backend.service';
import { SessionService } from '../../services/session.service';
import { CryptoService, generateDeterministicNode, Keychain } from '../../services/crypto.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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
      .subscribe((keychains) => this.keychains = keychains);
  }

  public save(value): void {
    const keychain: Keychain = {
      name: value.name,
      key: this.cryptoService.encrypt(this.sessionService.getKey(), value.masterKey),
      positions: []
    };
    this.backendService.createKeychain(keychain).subscribe(() => this.ngOnInit());
  }

  get name() {
    return this.keychainForm.get('name');
  }

  get masterKey() {
    return this.keychainForm.get('masterKey');
  }
}
