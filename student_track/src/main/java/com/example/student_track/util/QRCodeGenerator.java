package com.example.student_track.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.File;
import java.nio.file.Path;

public class QRCodeGenerator {

    public static String generateQRCodeFile(String text, String uploadDir) throws Exception {
        // Ensure folder exists
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        // Unique file name
        String fileName = text + ".png";
        Path path = new File(uploadDir + "/" + fileName).toPath();

        // Save QR image
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return "/uploads/qr/" + fileName;
    }
}
