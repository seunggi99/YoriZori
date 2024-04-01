package KNU.YoriZori.dto;

import KNU.YoriZori.domain.StoragePlace;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdatePutInRequestDto {

    public LocalDate expDate;
    public LocalDate putDate;
    private StoragePlace storagePlace;

}
