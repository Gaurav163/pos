package com.increff.pos.model;

<<<<<<< HEAD
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandForm {
    private Long id;
    private String name;
    private String category;
=======
<<<<<<<< HEAD:src/main/java/com/increff/pos/dto/BrandDto.java

public class BrandDto {

========
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class BrandForm {
    @NotBlank()
    private String name;
    @NotBlank()
    private String category;
>>>>>>>> 7bf20482a1dddcb1cabededf8c6b2c1868cb013d:pos/src/main/java/com/increff/pos/model/BrandForm.java
>>>>>>> 7bf20482a1dddcb1cabededf8c6b2c1868cb013d
}
