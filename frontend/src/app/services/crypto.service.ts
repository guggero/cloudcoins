import * as randomBytes from 'randombytes';
import { crypto, HDNode, networks } from 'bitcoinjs-lib';
import { Injectable } from '@angular/core';
import { pbkdf2Sync } from 'pbkdf2';
import { AES, enc, HmacSHA512 } from 'crypto-js';
import { generateMnemonic, mnemonicToSeed } from 'bip39';
import { Network } from '../networks';
import { Account } from './account';

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

export interface KeyPosition {
  id: number;
  coinType: number;
  index: number;
  custom: boolean;
}

export interface KeyPair {
  index: number;
  wif: string;
  address: string;
}

export interface Coin {
  coinType: number;
  hdNode: any;
  network: Network;
  keyPairs: KeyPair[];
}

export interface Keychain {
  id?: number;
  name: string;
  key: string;
  hdRootNode?: any;
  hdBip44Node?: any;
  createdAt?: Date;
  positions: KeyPosition[];
  coins?: Coin[];
}

@Injectable()
export class CryptoService {

  public createAccount(username: string, password: string, otpAuthKey: string): Account {
    const salt = createSalt(16);
    const saltStr = salt.toString('hex');
    const pwHash1 = sha256String(saltStr + password);
    const pwHash2 = sha256String(saltStr + pwHash1.toString('hex'));
    return new Account(username, saltStr, pwHash2.toString('hex'), otpAuthKey);
  }

  public getLoginData(username: string, password: string, salt: string): Account {
    const pwHash1 = sha256String(salt + password);
    const pwHash2 = sha256String(salt + pwHash1.toString('hex'));
    return new Account(username, salt, pwHash2.toString('hex'));
  }

  public decrypt(key: string, encrypted: string): string {
    return AES.decrypt(encrypted, key).toString(enc.Utf8);
  }

  public encrypt(key: string, plaintext: string): string {
    return AES.encrypt(plaintext, key).toString();
  }
}
