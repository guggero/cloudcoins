-- the password for the test account is abcABC123
INSERT INTO account (id, username, salt, password, otpauthkey) VALUES
  (nextval('account_seq'), 'guggero', '173c25d9a03b3b95ef7fcba0c5f90a2c',
   '6c5f4394e215706a1a283974f730001775ee691bfab0cc573eea19f8ec2cc353',
   'OY2UCJRSG44TO5DXKFFWCMKRPVHGKVSNONUSCRDDKQ2FUN3WMNVQ');
