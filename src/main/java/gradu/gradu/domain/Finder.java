package gradu.gradu.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class Finder {
    @Id
    private Long id;
    private String missingName;
    private String missingAge;
    private String missingNum;
    private String missingGender;
    private String missingPlace;
    private String missingDate;
}
