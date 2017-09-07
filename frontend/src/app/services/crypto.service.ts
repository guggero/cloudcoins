import * as randomBytes from 'randombytes';
import { crypto, HDNode, networks } from 'bitcoinjs-lib';
import { Injectable } from '@angular/core';
import { pbkdf2Sync } from 'pbkdf2';
import { AES, enc, HmacSHA512 } from 'crypto-js';
import { generateMnemonic, mnemonicToSeed } from 'bip39';


export const PBKDF2_HMAC_LEN = 64;
export const PBKDF2_ROUNDS = 10240;
export const PBKDF2_DIGEST = 'sha512';

export function createSalt(length: number): Buffer {
  return randomBytes(length);
}

export function sha256String(str: string): Buffer {
  return crypto.sha256(Buffer.from(str, 'utf8'));
}

export function sha256Buffer(buffer: Buffer): Buffer {
  return crypto.sha256(buffer);
}

export function createEncryptionKey(password: string, salt: string): Buffer {
  return strenghtenPasswordPBKDF2(sha256String(salt + password), Buffer.from(salt, 'hex'));
}

export function strenghtenPasswordPBKDF2(password: Buffer, salt: Buffer): Buffer {
  return pbkdf2Sync(password, salt, PBKDF2_ROUNDS, PBKDF2_HMAC_LEN, PBKDF2_DIGEST);
}

export function generateDeterministicNode(): string {
  const seed = mnemonicToSeed(generateMnemonic());
  const node = HDNode.fromSeedBuffer(seed, networks.bitcoin);
  return node.toBase58();
}

export function parseNode(base58: string): any {
  return HDNode.fromBase58(base58, networks.bitcoin);
}

export function hmacSha512(sha512: string): any {
  return HmacSHA512('Seed version', sha512);
}

export class Account {
  constructor(public email: string,
              public salt: string,
              public backendIdentification: string,
              public otpAuthKey?: string) {
  }

  public getPersistablePart(): any {
    return {
      email: this.email,
      salt: this.salt,
      password: this.backendIdentification,
      otpAuthKey: this.otpAuthKey,
    };
  }
}

export interface KeyPair {
  privKeyWif: string;
  address: string;
}

export interface KeyPosition {
  id: number;
  coinType: number;
  index: number;
  keyPairs?: KeyPair[];
}

export interface Keychain {
  id?: number;
  name: string;
  key: string;
  decryptedKey?: any;
  createdAt?: Date;
  positions: KeyPosition[];
}

@Injectable()
export class CryptoService {

  public createAccount(email: string, password: string, otpAuthKey: string): Account {
    const salt = createSalt(16);
    const saltStr = salt.toString('hex');
    const pwHash1 = sha256String(saltStr + password);
    const pwHash2 = sha256String(saltStr + pwHash1.toString('hex'));
    return new Account(email, saltStr, pwHash2.toString('hex'), otpAuthKey);
  }

  public getLoginData(email: string, password: string, salt: string): Account {
    const pwHash1 = sha256String(salt + password);
    const pwHash2 = sha256String(salt + pwHash1.toString('hex'));
    return new Account(email, salt, pwHash2.toString('hex'));
  }

  public decrypt(key: string, encrypted: string): string {
    return AES.decrypt(encrypted, key).toString(enc.Utf8);
  }

  public encrypt(key: string, plaintext: string): string {
    return AES.encrypt(plaintext, key).toString();
  }
}
