ALTER TABLE refresh_tokens DROP CONSTRAINT chk_tipo_usuario;
ALTER TABLE refresh_tokens ADD CONSTRAINT chk_tipo_usuario CHECK (tipo_usuario IN ('COMERCIO','BENEFICIARIO','COMPRADOR','ADMIN'));
