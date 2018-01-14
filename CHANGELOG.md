# Changelog

## 2018-01-14

**Implemented enhancements:**
 - Add changelog
 - Add button to delete a keychain and all derived addresses
 - Update timeline to show planned activities for Q1 2018 and beyond

**Fixed bugs:**
 - Add `autocomplete="false"` to sensitive fields so the browser doesn't store values entered there

## 2017-12-31

**Implemented enhancements:**
 - Add Potcoin

## 2017-12-23

**Implemented enhancements:**
 - Implement checksum for Ethereum addresses

## 2017-12-21

**Implemented enhancements:**
 - Describe how to set up development environment (thanks to [lackrobin](https://github.com/lackrobin))

**Fixed bugs:**
 - Rename the coin HTML5 to new and correct name HTML

## 2017-12-19

**Implemented enhancements:**
 - Fix key calculation of ARK, they don't hash a public key with SHA256 to generate an address (only ripemd160)

## 2017-12-28
 
 **Implemented enhancements:**
  - Add ARK

## 2017-12-12

**Implemented enhancements:**
 - Show generic error message when a communication error happens

## 2017-12-11

**Implemented enhancements:**
 - Add form to import keychain from Coinomi mobile app

## 2017-12-09

**Implemented enhancements:**
 - Improve layout of address overview and QR codes

**Fixed bugs:**
 - Fix favicons that were broken since the upgrade to Angular 5

## 2017-12-08

**Implemented enhancements:**
 - Show QR code for addresses and private keys

## 2017-12-07

**Implemented enhancements:**
 - Show balance for some coins that are supported by the [cryptoID](https://chainz.cryptoid.info/api.dws) API

## 2017-12-05

**Fixed bugs:**
 - Fix bug that prevented the keys to be formatted correctly

## 2017-12-04

**Implemented enhancements:**
 - Completely refactor key generation, derive BIP44 keys in multiple steps along the tree
   so not every key has to be derived starting at the root

## 2017-12-03

**Implemented enhancements:**
 - Add Ethereum (ETH) and its custom formatted addresses and private keys

## 2017-11-29

**Fixed bugs:**
 - Fix problem with Angular trying to parse empty JSON in response

## 2017-11-28

**Implemented enhancements:**
 - Finish upgrade to Angular 5: Fix title, UglifyJS options for BitcoinJS library and asset path

## 2017-11-27

**Implemented enhancements:**
 - Start upgrading to Angular 5

**Fixed bugs:**
 - Fix Travis build

## 2017-11-26

**Implemented enhancements:**
 - Key pairs with a custom BIP44 index can now be added to the keychain
 - Add HTML5 coin
 - Show BIP44 index of key pairs in the keychain
 - Upgrade project to Angular 4

## 2017-11-25

**Implemented enhancements:**
 - Update network configurations, add custom base58check hash function that is needed by SmartCash

## 2017-10-09

**Fixed bugs:**
 - Fix build

## 2017-10-06

**Implemented enhancements:**
 - Add list of different coins so addresses for them can be generated
 - Improve user interface

## 2017-09-22

**Implemented enhancements:**
 - Rename email to username for account registration
 - Make username unique in database
 - Secure contact form with ReCaptcha
 - Add link to test OTP code for development

## 2017-09-07

**Implemented enhancements:**
 - Build only master branch on Travis
 - Add QR code of OTP seed for test account
 - Begin with multi coin address generation

## 2017-09-05

**Fixed bugs:**
 - Fix minor bugs

## 2017-09-03

**Implemented enhancements:**
 - Fix production build of frontend (webpack)
 - Add backend and frontend to create and store encrypted BIP44 master keys
 - Add 2-factor authentication

## 2017-09-02

**Implemented enhancements:**
 - Add backend and frontend to create an account and log in

## 2017-09-01

**Implemented enhancements:**
 - Create project skeleton with frontend, backend and docker build
 - Add static content, project description
 - Set up build on Travis

## 2017-08-31

**Implemented enhancements:**
 - Switch license to Apache 2.0

## 2017-08-29

**Implemented enhancements:**
 - Initial commit