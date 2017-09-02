import { Component } from '@angular/core';
import { AppService } from '../../app.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { matchOtherValidator, matchValueValidator } from '../../ui-components/functions';
import { BackendService } from '../../services/backend.service';
import { CryptoService } from '../../services/crypto.service';
import { EMAIL_REG_EXP } from '../../ui-components/globals';

@Component({
  selector: 'create-account',
  providers: [],
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent {

  public accountForm: FormGroup;
  public showSuccessMessage: boolean = false;
  public errorMessage: any = null;

  constructor(private backendService: BackendService, private cryptoService: CryptoService, private formBuilder: FormBuilder) {
    this.accountForm = formBuilder.group({
      email: ['', [Validators.required, Validators.pattern(EMAIL_REG_EXP)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, matchOtherValidator('password')]],
      confirmation: [
        '',
        [Validators.required, matchValueValidator('I understand that nobody can restore my password if I forget it')]],
    });
  }

  public createAccount(value) {
    const account = this.cryptoService.createAccount(value.email, value.password);
    this.backendService.createAccount(account.getPersistablePart())
      .subscribe(() => this.onSuccess(), (err) => this.onError(err));
  }

  private onSuccess() {
    this.errorMessage = null;
    this.showSuccessMessage = true;
  }

  private onError(err: any) {
    this.showSuccessMessage = false;
    this.errorMessage = err;
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
