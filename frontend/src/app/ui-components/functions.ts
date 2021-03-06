import { FormControl } from '@angular/forms';

export function matchOtherValidator(otherControlName: string) {

  let thisControl: FormControl;
  let otherControl: FormControl;

  return (control: FormControl) => {

    if (!control.parent) {
      return null;
    }

    // Initializing the validator.
    if (!thisControl) {
      thisControl = control;
      otherControl = control.parent.get(otherControlName) as FormControl;
      if (!otherControl) {
        throw new Error('matchOtherValidator(): other control is not found in parent group');
      }
      otherControl.valueChanges.subscribe(() => {
        thisControl.updateValueAndValidity();
      });
    }

    if (!otherControl) {
      return null;
    }

    if (otherControl.value !== thisControl.value) {
      return {
        matchOther: true
      };
    }

    return null;

  };
}

export function matchValueValidator(value: string) {

  return (control: FormControl) => {
    if (control.value !== value) {
      return {
        matchValue: true
      };
    }
    return null;
  };
}

export function createErrorHandler(component: any, scrollUp = true): (error: any) => void {
  return (response: any): void => {
    if (!response || !response.error) {
      return;
    }

    let errors = response.error.errors;
    if (errors && errors.length) {
      component.notificationKey = errors[0].messageKey;
    } else if (response.error) {
      component.notificationKey = 'ch.cloudcoins.errors.connectionError';
    }
    component.isSuccess = false;
    component.buttonClicked = false;
    if (scrollUp) {
      window.scrollTo(0, 0);
    }
  };
}

export function formatString(str: string, ...args: string[]): string {
  let i = 0;

  return str.replace(/%s/g, function () {
    return args[i++];
  });
}
