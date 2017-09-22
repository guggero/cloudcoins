-- the password for the test account is abcABC123
-- open the following link and scan QR code in the image with your OTP app to generate codes for the test account
-- https://chart.googleapis.com/chart?cht=qr&chl=otpauth%3A%2F%2Ftotp%2FCloudcoins%2520Test%3Fsecret%3DOY2UCJRSG44TO5DXKFFWCMKRPVHGKVSNONUSCRDDKQ2FUN3WMNVQ%26algorithm%3DSHA1%26digits%3D6%26period%3D30&chs=180x180&choe=UTF-8&chld=L|2

INSERT INTO account (id, username, salt, password, otpauthkey) VALUES
  (nextval('account_seq'), 'guggero', '173c25d9a03b3b95ef7fcba0c5f90a2c',
   '6c5f4394e215706a1a283974f730001775ee691bfab0cc573eea19f8ec2cc353',
   'OY2UCJRSG44TO5DXKFFWCMKRPVHGKVSNONUSCRDDKQ2FUN3WMNVQ');
