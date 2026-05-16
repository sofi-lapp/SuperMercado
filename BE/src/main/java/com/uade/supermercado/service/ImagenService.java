package com.uade.supermercado.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImagenService {

    private final Cloudinary cloudinary;

    public String subirImagen(MultipartFile archivo) {
        try {
            Map<?, ?> resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap("folder", "emarket/productos")
            );
            return (String) resultado.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary: " + e.getMessage());
        }
    }
}
