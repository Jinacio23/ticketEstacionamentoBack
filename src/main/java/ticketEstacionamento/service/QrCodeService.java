package ticketEstacionamento.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketEstacionamento.entity.Ticket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class QrCodeService {

    @Autowired
    private TicketService ticketService;

    public byte[] generateQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    /*
    public String generateQRCodeBase64(String text, int width, int height) throws WriterException, IOException {
        byte[] qrCode = generateQRCode(text, width, height);
        return Base64.getEncoder().encodeToString(qrCode);
    }
     */

    public byte[] generateTicketQrCode(Long ticketId) throws Exception {
        Ticket ticket = ticketService.findById(ticketId);

        if(ticket.getQrCodeToken() == null){
        // Gera um token único com tempo de expiração
        String token = UUID.randomUUID().toString();
        ticket.setQrCodeToken(token);
        ticket.setQrCodeExpiration(LocalDateTime.now().plusHours(2));
        ticketService.save(ticket);

        // Cria o conteúdo do QR Code
        String qrContent = String.format("ticket:%s,token:%s", ticket.getIdTicket(), token);

        // Gera e retorna o QR Code
        return this.generateQRCode(qrContent, 200, 200);

        } else {
            throw new BadRequestException("Token do QrCode já gerado para ticket ID: " + ticket.getIdTicket());
        }
    }
}
