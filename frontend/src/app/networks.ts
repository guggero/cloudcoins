import { networks } from 'bitcoinjs-lib';

export const NETWORKS = [
  {
    label: 'BTC (Bitcoin)',
    config: {
      ...networks.bitcoin,
      bip44: 0x00,
    }
  }, {
    label: 'BTC (Bitcoin Testnet)',
    config: {
      ...networks.testnet,
      bip44: 0x01,
    }
  }, {
    label: 'LTC (Litecoin)',
    config: {
      ...networks.litecoin,
      bip44: 0x02,
    }
  },
  {
    label: 'DASH (Dash)',
    config: {
      messagePrefix: 'unused',
      bip32: {public: 0x0488b21e, private: 0x0488ade4},
      pubKeyHash: 0x4C,
      scriptHash: 0x10,
      wif: 0xcc,
      bip44: 0x05,
    }
  }, {
    label: 'DOGE (Dogecoin)',
    config: {
      messagePrefix: 'unused',
      bip32: {public: 0x02facafd, private: 0x02fac398},
      pubKeyHash: 0x1e,
      scriptHash: 0x16,
      wif: 0x9e,
      bip44: 0x03,
    }
  }, {
    label: 'PIVX (PIVX)',
    config: {
      messagePrefix: 'unused',
      bip32: {public: 0x022d2533, private: 0x0221312b},
      pubKeyHash: 0x1E,
      scriptHash: 0x0D,
      wif: 0xd4,
      bip44: 0x77,
    }
  }, {
    label: 'XVG (Verge)',
    config: {
      messagePrefix: 'unused',
      bip32: {public: 0x0488b21e, private: 0x0488ade4},
      pubKeyHash: 0x1E,
      scriptHash: 0x21,
      wif: 0x9e,
      bip44: 0x4d,
    }
  }, {
    label: 'VIA (Viacoin)',
    config: {
      messagePrefix: 'unused',
      bip32: {
        public: 0x0488b21e, private: 0x0488ade4
      },
      pubKeyHash: 0x47,
      scriptHash: 0x21,
      wif: 0xc7,
      bip44: 0x0e,
    }
  }
];
