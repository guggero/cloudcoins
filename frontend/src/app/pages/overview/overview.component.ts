import { Component, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BackendService } from '../../services/backend.service';
import { EMAIL_REG_EXP } from '../../ui-components/globals';
import { createErrorHandler } from '../../ui-components/functions';
import { ReCaptchaComponent } from 'angular2-recaptcha';

@Component({
  selector: 'overview',
  providers: [],
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent {

  public messageForm: FormGroup;
  public notificationKey: string = null;
  public isSuccess: boolean = false;
  @ViewChild(ReCaptchaComponent) public captcha: ReCaptchaComponent;

  constructor(private backendService: BackendService, private formBuilder: FormBuilder) {
    this.messageForm = formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [Validators.required, Validators.pattern(EMAIL_REG_EXP)]],
      message: ['', [Validators.required]]
    });
  }

  public createMessage(value: any): void {
    this.backendService.createMessage(this.captcha.getResponse(), value)
      .subscribe(() => this.onSuccess(), createErrorHandler(this, false));
  }

  private onSuccess() {
    this.notificationKey = 'send-message.successful';
    this.isSuccess = true;
  }

  get name() {
    return this.messageForm.get('name');
  }

  get email() {
    return this.messageForm.get('email');
  }

  get message() {
    return this.messageForm.get('message');
  }
}
