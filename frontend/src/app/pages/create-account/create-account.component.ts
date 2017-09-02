import { Component } from '@angular/core';
import { AppService } from '../../app.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { matchOtherValidator, matchValueValidator } from '../../ui-components/functions';
import { BackendService } from '../../services/backend.service';
import { CryptoService } from '../../services/crypto.service';

export const EMAIL_REG_EXP: RegExp = /.*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?[.]+)+(?:[a-z0-9-]{2,})+?$/;

@Component({
  selector: 'create-account',
  providers: [],
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent {

  public accountForm: FormGroup;

  constructor(private backendService: BackendService, private cryptoService: CryptoService, private formBuilder: FormBuilder) {
    this.accountForm = formBuilder.group({
      email: ['gugger@gmail.com', [Validators.required, Validators.pattern(EMAIL_REG_EXP)]],
      password: ['abcABC123', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['abcABC123', [Validators.required, matchOtherValidator('password')]],
      confirmation: [
        'I understand that nobody can restore my password if I forget it',
        [Validators.required, matchValueValidator('I understand that nobody can restore my password if I forget it')]],
    });
  }

  public createAccount(value) {
    const salt = this.cryptoService.createSalt(16);
    const pwHash1 = this.cryptoService.sha256String(salt + value.password);
    const pwHash2 = this.cryptoService.sha256String(salt + pwHash1.toString('hex'));

  }

  get email() {
    return this.accountForm.get('email');
  }

  get password() {
    return this.accountForm.get('password');
  }

  get passwordRepeat() {
    return this.accountForm.get('passwordRepeat');
  }

  get confirmation() {
    return this.accountForm.get('confirmation');
  }
}
