import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BackendService } from '../../services/backend.service';
import { CryptoService } from '../../services/crypto.service';
import { EMAIL_REG_EXP } from '../../ui-components/globals';

@Component({
  selector: 'login',
  providers: [],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  public loginForm: FormGroup;
  public showSuccessMessage: boolean = false;
  public errorMessage: any = null;

  constructor(private backendService: BackendService, private cryptoService: CryptoService, private formBuilder: FormBuilder) {
    this.loginForm = formBuilder.group({
      email: ['', [Validators.required, Validators.pattern(EMAIL_REG_EXP)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      otp: ['', [Validators.required]]
    });
  }

  public login(value) {
    const errFn = (err) => this.onError(err);
    this.backendService.getSalt(value.email, value.otp)
      .subscribe((salt) => {
        this.backendService.login(this.cryptoService.getLoginData(value.email, value.password, salt))
          .subscribe(() => this.onSuccess(), errFn);
      }, errFn);
  }

  private onSuccess() {
    this.errorMessage = null;
    this.showSuccessMessage = true;
    window.scrollTo(0, 0);
  }

  private onError(err: any) {
    this.showSuccessMessage = false;
    this.errorMessage = err;
    window.scrollTo(0, 0);
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get otp() {
    return this.loginForm.get('otp');
  }
}
