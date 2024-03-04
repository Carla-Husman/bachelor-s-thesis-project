import { Injectable } from '@angular/core';
import CryptoJS from 'crypto-js';
import {ConsoleLogger} from "@angular/compiler-cli";

@Injectable({
  providedIn: 'root'
})
export class EncrDecrService {
  keys = "998NP7xc5xZGitlOJJ1pNBt6xZiX"

  constructor() {
  }

  encrypt(value: { toString: () => string; }){
    const key = CryptoJS.enc.Utf8.parse(this.keys);
    const iv = CryptoJS.enc.Utf8.parse(this.keys);
    const encrypted = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(value.toString()), key,
      {
        keySize: 128 / 8,
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
      });

    return encrypted.toString();
  }

  decrypt(value: string | CryptoJS.lib.CipherParams){
    const key = CryptoJS.enc.Utf8.parse(this.keys);
    const iv = CryptoJS.enc.Utf8.parse(this.keys);
    const decrypted = CryptoJS.AES.decrypt(value, key, {
      keySize: 128 / 8,
      iv: iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    });
    return decrypted.toString(CryptoJS.enc.Utf8);
  }
}
