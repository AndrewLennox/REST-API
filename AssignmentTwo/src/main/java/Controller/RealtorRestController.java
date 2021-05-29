/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import Exception.PropNotFound;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Model.Properties;
import Model.Propertytypes;
import Model.Agents;
import Repository.PropRepo;
import Service.AgentService;
import Service.PropService;
import Service.ProptypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpHeaders;

/**
 *
 * @author Andrew
 */
@RestController
@RequestMapping("/prop")
public class RealtorRestController {
        
    @Autowired
    private PropService propService;
    
    @Autowired
    private AgentService agService;
    
    @Autowired
    private ProptypeService ptService;
    
    @Autowired
    private PropRepo propRepo;
    
        @Operation(summary = "Get all propertys")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found properties", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Propertys", 
    content = @Content) })
    @GetMapping("")
    public List<Properties> getAll() {
        //throw new ApiRequestException("Oops caannot find properties");
        return propService.findAll();
    }

    
    //when followed will display the street, city, price and agents email address.
        @Operation(summary = "will display the street, city, price and agents email address.")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found property", 
    content = { @Content(mediaType = "string", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
  @GetMapping("details/{id}")
    public String getOneDetail(@PathVariable long id) {
        try{
        Properties prop = propService.findOne(id);
        
        long x = (long)prop.getAgent_Id();
        Agents a = agService.findOne(x);
        
        String r = "Street="+prop.getStreet()+" City="+prop.getCity()+" Price="+prop.getPrice()+" Agent Email ="+ a.getEmail();
                return r;
        }
        catch(Exception e){
        throw new ApiRequestException("Oops caannot find selected property");
        }
    }
    
    // Getting a single item HATEOAS
    @Operation(summary = "Get a property by its id (HATEOAS)")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Property", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
           @GetMapping("/property/{id}")
    EntityModel<Optional<Properties>> one(@PathVariable Long id) {

        Optional<Properties> employee = propRepo.findById(id); //

        try{
        return EntityModel.of(employee, //
                linkTo(methodOn(RealtorRestController.class).one(id)).withSelfRel(),
                linkTo(methodOn(RealtorRestController.class).getAll()).withRel("http://localhost:8080/prop/allproperties"));
        }catch(Exception e){
        throw new ApiRequestException("Oops cannot find selected property");
        }
    }
    
    //street, city, price and agents email address
    
    
    
    // HATEOAS
            @Operation(summary = "Get all propertys (HATEOAS)")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found properties", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Propertys not found", 
    content = @Content) })
    @GetMapping("/allproperties")
    CollectionModel<EntityModel<Properties>> all() {
        try{
        List<EntityModel<Properties>> props = propService.findAll().stream()
                .map(p -> EntityModel.of(p,
                linkTo(methodOn(RealtorRestController.class).one(p.getId())).withSelfRel(),
                linkTo(methodOn(RealtorRestController.class).all()).withRel("http://localhost:8080/prop/details/"+p.getId())))
                .collect(Collectors.toList());

        return CollectionModel.of(props, linkTo(methodOn(RealtorRestController.class).all()).withSelfRel());
        }catch(Exception e){
        throw new ApiRequestException("Oops cannot find all properties");
        }
    }
    
    
                @Operation(summary = "Returns total number of propertys")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found total properties", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Propertys not found", 
    content = @Content) })
    @GetMapping("/count")
    public long getCount() {
        return propService.count();
    }
    
    
                    @Operation(summary = "Deletes selected property")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Deleted", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        try{
        propService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);
        }catch(Exception e){
        throw new ApiRequestException("Property never existed in the first place, invalid URL");
        }
    }
    
    
                        @Operation(summary = "Add a new property")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Property added", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid input", 
    content = @Content), 
 })
    @PostMapping("")
    public ResponseEntity add(@RequestBody Properties a) {
        try{
        propService.saveAuthor(a);
        return new ResponseEntity(HttpStatus.CREATED);
        }catch(Exception e){
        throw new ApiRequestException("Add Failure, input may be incorrect");
        }
    }
    
                    @Operation(summary = "Edit selected property")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Edited successfully", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid input", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
    @PutMapping("")
    public ResponseEntity edit(@RequestBody Properties a) {
        try{
        propService.saveAuthor(a);
        return new ResponseEntity(HttpStatus.OK);
        }catch(Exception e){
        throw new ApiRequestException("Edit failure, potential incorrect input");
        }
    }
    
        // RETURN XML
     @Operation(summary = "Get a property by its id (XML FORMAT)")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Property", 
    content = { @Content(mediaType = "application/xml", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
    @GetMapping("xml/{id}")
    @Produces(MediaType.APPLICATION_XML_VALUE)
    public Properties getoneXMLList(@PathVariable long id) {
        try{
        return propService.findOne(id);
        }catch(Exception e){
        throw new ApiRequestException("Oops cannot find selected property");
        }
    }
    

    
    
    //public ResponseEntity<BufferedImage> zxingQRCode(@RequestBody Agents barcode)throws Exception{
     @Operation(summary = "Returns QR Code with Agent Name and Phone Number")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "QR CODE", 
    content = { @Content(mediaType = "application/png", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found relative to agent", 
    content = @Content) })
    @PostMapping(value = "/zxing/qrcode/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> zxingQRCode(@PathVariable long id)throws Exception{
        //System.out.println(id);
                    //System.out.println(barcode);
          try{          
        Properties prop1 = new Properties();
        prop1 = propService.findOne(id);
        
        long l = prop1.getAgent_Id();
        //int vid =  (int)l;
        
        Agents barcode = new Agents();
        barcode = agService.findOne(l);
        
        return successResponse(CodeGenerator.generatedQRCode(barcode));
          }catch(Exception e){
        throw new ApiRequestException("QR Generation failure, check parameters");
        }
    }
    
    private ResponseEntity<BufferedImage> successResponse(BufferedImage image) {
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
    
    @Bean 
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter(){
    return new BufferedImageHttpMessageConverter();
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    
    
    
     @Operation(summary = "Replace a property by its id")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Property", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
    @PutMapping("/replace/{id}")
    Properties replaceProperties(@RequestBody Properties newEmployee, @PathVariable Long id) {
        try{
        return propRepo.findById(id)
                .map(employee -> {
                    employee.setCity(newEmployee.getCity());
                    employee.setPrice (newEmployee.getPrice());
                    return propRepo.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return propRepo.save(newEmployee);
                });
        }catch(Exception e){
        throw new ApiRequestException("Oops cannot replace propertiy, potential incorrect input");
        }
    }

     @Operation(summary = "Properties filtered by Agent")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Property", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
    @GetMapping("/byagent/{id}")
    public List<Properties> propertiesByAgent(@PathVariable long id) {
        try{
        int x = (int)id;
        return propService.byAgent(x);
        }catch(Exception e){
        throw new ApiRequestException("Cannot find agent");
        }
    }

    
    // WORKING GET AGENT PICTURE
     @Operation(summary = "Get Image for a given agent")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Agent", 
    content = { @Content(mediaType = "application/jpeg", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Agent not found", 
    content = @Content) })
    @GetMapping(
            value = "/getagent/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType(@PathVariable long id) throws IOException {
        try{
        Agents a = agService.findOne(id);
        InputStream in = getClass()
                .getResourceAsStream("/static/images/agents/"+a.getAgent_Id()+".jpg");
        return IOUtils.toByteArray(in);
        }catch(Exception e){
        throw new ApiRequestException("No agent found");
        }
    }
    
    // WORKING GET IMAGE FOR PROPERTY
     @Operation(summary = "Get a property Image")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Property", 
    content = { @Content(mediaType = "application/jpeg", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
    @GetMapping(
            value = "/getimage/{mm}/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImage(@PathVariable String mm,@PathVariable long id) throws IOException { 
        try{
        Properties p = propService.findOne(id);
        //System.out.println(p.getPhoto());
        //System.out.println(p.getListing_Num());        
        String value = "";       
        if(mm.contains("large")){
        value = "/static/images/properties/large/"+p.getListing_Num()+"/"+p.getPhoto();
        }else{
        value = "/static/images/properties/thumbs/"+p.getPhoto();
        }        
        InputStream in = getClass()
                .getResourceAsStream(value);
        return IOUtils.toByteArray(in);
        }catch(Exception e){
        throw new ApiRequestException("Oops cannot find property");
        }
    }


    
        //
//    @GetMapping(
//            value = "/proptype/{id}",
//            produces = MediaType.APPLICATION_XML_VALUE
//    )
    
     @Operation(summary = "Properties filtered by Property Type")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Property Type", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property type not found", 
    content = @Content) })
    @GetMapping("/ptype/{id}")
    public List<Properties> PropertybyType(@PathVariable String id){ 
        try{
        Propertytypes p = ptService.findName(id);
        int x = p.getType_Id();
        return propService.byType(x);
        }catch(Exception e){
        throw new ApiRequestException("Invalid type");
        }
    }
    
    
    
         @Operation(summary = "Properties filtered by number of bathrooms and Bedrooms")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Property Type", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property type not found", 
    content = @Content) })
        @GetMapping("/bb/{mm}/{id}")
    public List<Properties> BathroomBedroom(@PathVariable int mm,@PathVariable float id){ 
        
        try{
        return propService.BB(mm, id);
                }catch(Exception e){
        throw new ApiRequestException("Invalid parameters");
        }
    }
    

             @Operation(summary = "Returns Property Brochure in PDF format")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Property", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
        @GetMapping(value = "/pdf/{id}",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity customersReport(@PathVariable Long id) throws IOException {
        //List customers = (List) propService.findAll();
        try{
        Properties prop = propService.findOne(id);
        ByteArrayInputStream bis = PDFGenerator.customerPDFReport(prop);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=property.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
        }catch(Exception e){
        throw new ApiRequestException("Inalid input, no property found");
        }
    }
    
    
    
    @Operation(summary = "Returns zipped file of images related to property and LIT Logo")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the Property", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Properties.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Property not found", 
    content = @Content) })
     @RequestMapping(value = "/zip/{id}", produces="application/zip")
    public byte[] zipFiles(HttpServletResponse response,@PathVariable Long id) throws IOException{
        
        //setting headers
        Properties prop = propService.findOne(id);
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"images.zip\"");

        //byteArray stream, make it bufforable and passing this buffor to ZipOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        
        ArrayList<File> files = new ArrayList<>();
        files.add(new File("src/main/resources/static/images/site/logo.gif"));
        files.add(new File("src/main/resources/static/images/properties/large/"+prop.getListing_Num()+"/"+prop.getListing_Num()+"-1.JPG"));
        files.add(new File("src/main/resources/static/images/properties/large/"+prop.getListing_Num()+"/"+prop.getListing_Num()+"-2.JPG"));
        files.add(new File("src/main/resources/static/images/properties/large/"+prop.getListing_Num()+"/"+prop.getListing_Num()+"-3.JPG"));
        files.add(new File("src/main/resources/static/images/properties/large/"+prop.getListing_Num()+"/"+prop.getListing_Num()+"-4.JPG"));
                files.add(new File("src/main/resources/static/images/properties/large/"+prop.getListing_Num()+"/"+prop.getListing_Num()+"-5.JPG"));
                        files.add(new File("src/main/resources/static/images/properties/large/"+prop.getListing_Num()+"/"+prop.getPhoto()));



                

        
        //pack files
        for (File file : files) {
            //new zip entry and copying inputstream with file to zipOutputStream
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
