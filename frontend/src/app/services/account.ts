export class Account {
  constructor(public username: string,
              public salt: string,
              public backendIdentification: string,
              public otpAuthKey?: string) {
  }

  public getPersistablePart(): any {
    return {
      username: this.username,
      salt: this.salt,
      password: this.backendIdentification,
      otpAuthKey: this.otpAuthKey,
    };
  }
}
