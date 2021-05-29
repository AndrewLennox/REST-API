/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Model.Properties;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Andrew
 */
public class PDFGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(PDFGenerator.class);
	
	public static ByteArrayInputStream customerPDFReport(Properties prop) throws IOException {
		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
        	
        	PdfWriter.getInstance(document, out);
            document.open();
        	
            // title
        	Font font = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 20, BaseColor.BLACK);
        	Paragraph para = new Paragraph( "Property Brochure", font);
        	para.setAlignment(Element.ALIGN_CENTER);
        	document.add(para);
        	document.add(Chunk.NEWLINE);
                
                
                

                  //Add Image
                  File f = new File("src/main/resources/static/images/properties/large/"+prop.getListing_Num()+"/"+prop.getPhoto());
    Image image1 = Image.getInstance(f.toURI().toString());

    image1.setAlignment(Element.ALIGN_CENTER);

    image1.scaleAbsolute(400, 250);

    document.add(image1);
    document.add(Chunk.NEWLINE);
    
    
//Location
        	Font fce = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
        	Paragraph pc = new Paragraph("City : " + prop.getCity() + "  Street : " + prop.getStreet(), fce);
        	pc.setAlignment(Element.ALIGN_CENTER);
        	document.add(pc);
        	document.add(Chunk.NEWLINE);
    
    
    // Add Description

        	Font fonte = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 13, BaseColor.BLACK);
        	Paragraph parae = new Paragraph( prop.getDescription(), fonte);
        	para.setAlignment(Element.ALIGN_CENTER);
        	document.add(parae);
        	document.add(Chunk.NEWLINE);
        	
        	PdfPTable table = new PdfPTable(4);
        	
            Stream.of("Lot Size", "Bathroom","Bedrooms", "Garage Size")
	            .forEach(headerTitle -> {
		              PdfPCell header = new PdfPCell();
		              Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
		              header.setBackgroundColor(BaseColor.YELLOW);
		              header.setHorizontalAlignment(Element.ALIGN_CENTER);
		              header.setBorderWidth(2);
		              header.setPhrase(new Phrase(headerTitle, headFont));
		              table.addCell(header);
	            });
            
            //for (Properties p :) {
            	PdfPCell lot = new PdfPCell(new Phrase(prop.getLotsize()));
            	
            	lot.setVerticalAlignment(Element.ALIGN_MIDDLE);
            	lot.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(lot);

                PdfPCell bath = new PdfPCell(new Phrase(prop.getBathrooms().toString()));
                
                bath.setVerticalAlignment(Element.ALIGN_MIDDLE);
                bath.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(bath);
                
                PdfPCell bedCell = new PdfPCell(new Phrase(prop.getBedrooms().toString()));
                
                bedCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                bedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(bedCell);

                PdfPCell gsCell = new PdfPCell(new Phrase(String.valueOf(prop.getGaragesize())));
                gsCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                gsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                table.addCell(gsCell);
            //}
            document.add(table);
            
            // Price
            document.add(Chunk.NEWLINE);
                Font pr = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.GREEN);
        	Paragraph p = new Paragraph("Price : €" + prop.getPrice(), pr);
        	p.setAlignment(Element.ALIGN_CENTER);
        	document.add(p);
        	document.add(Chunk.NEWLINE);
            
                    // Logo 
        	Paragraph lit = new Paragraph("© LIT Realty",fce);
        	lit.setAlignment(Element.ALIGN_CENTER);
        	document.add(lit);
        	
                
    File logo = new File("src/main/resources/static/images/site/logo.gif");
    Image logo1 = Image.getInstance(logo.toURI().toString());
    logo1.setAlignment(Element.ALIGN_CENTER);
    logo1.scaleAbsolute(100, 60);
    document.add(logo1);

            
            
            document.close();
        }catch(DocumentException e) {
        	logger.error(e.toString());
        }
        
		return new ByteArrayInputStream(out.toByteArray());
	}
}