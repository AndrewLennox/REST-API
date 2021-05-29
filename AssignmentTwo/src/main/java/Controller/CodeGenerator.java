  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Agents;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andrew
 */
public class CodeGenerator {
    
    public static BufferedImage generatedQRCode(Agents urlText) throws Exception{
    
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode("Name :"+urlText.getName() + "PhoneNumber: "+ urlText.getPhone(), BarcodeFormat.QR_CODE, 200,200);
    
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
