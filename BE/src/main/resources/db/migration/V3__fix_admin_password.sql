-- Corrige el hash de la contraseña del admin (Admin1234!)
-- El hash de V2 era un placeholder incorrecto.
UPDATE usuarios
SET password_hash = '$2y$10$Fq.Fs94gUs.a3fj26iaGPe7WMkYUdCMNSH3MAar6jUCgPGZ2HXJT2'
WHERE email = 'admin@supermercado.com';
