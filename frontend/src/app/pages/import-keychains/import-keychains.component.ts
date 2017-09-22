import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'import-keychains',
  providers: [],
  templateUrl: './import-keychains.component.html',
  styleUrls: ['./import-keychains.component.scss']
})
export class ImportKeychainsComponent implements OnInit {

  public electrumForm: FormGroup;

  constructor(public sessionService: SessionService, private formBuilder: FormBuilder) {
    this.electrumForm = formBuilder.group({
      name: ['', [Validators.required]],
      electrumseed: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  public ngOnInit(): void {
    // nothing to do yet
  }

  get name() {
    return this.electrumForm.get('name');
  }

  get masterKey() {
    return this.electrumForm.get('masterKey');
  }
}
