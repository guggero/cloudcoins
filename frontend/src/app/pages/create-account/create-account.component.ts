import { Component } from '@angular/core';
import * as speakeasy from 'speakeasy';
import * as QRCode from 'qrcode';
import { AppService } from '../../app.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { createErrorHandler, matchOtherValidator, matchValueValidator } from '../../ui-components/functions';
import { BackendService } from '../../services/backend.service';
import { CryptoService } from '../../services/crypto.service';

@Component({
  selector: 'create-account',
  providers: [],
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent {

  public accountForm: FormGroup;
  public secret: any;
  public qrCode: string;
  public notificationKey: string = null;
  public isSuccess: boolean = false;

  constructor(private backendService: BackendService, private cryptoService: CryptoService, private formBuilder: FormBuilder) {
    this.accountForm = formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, matchOtherValidator('password')]],
      confirmation: [
        '',
        [Validators.required, matchValueValidator('I understand that nobody can restore my password if I forget it')]],
      otp: ['', [Validators.required]]
    });
    this.secret = speakeasy.generateSecret();
    QRCode.toDataURL(speakeasy.otpauthURL({secret: this.secret.ascii, label: 'Cloudcoins'}), (err, url) => {
      this.qrCode = url;
    });
  }

  public createAccount(value) {
    const account = this.cryptoService.createAccount(value.username, value.password, this.secret.base32);
    this.backendService.createAccount(account.getPersistablePart(), value.otp)
      .subscribe(() => this.onSuccess(), createErrorHandler(this));
  }

  private onSuccess() {
    this.notificationKey = 'create-account.successful';
    this.isSuccess = true;
    window.scrollTo(0, 0);
  }

  get username() {
    return this.accountForm.get('username');
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

  get otp() {
    return this.accountForm.get('otp');
  }
}
