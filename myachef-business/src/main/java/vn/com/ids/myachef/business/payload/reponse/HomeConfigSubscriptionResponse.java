package vn.com.ids.myachef.business.payload.reponse;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.dto.SubscriptionDTO;

@Getter
@Setter
@AllArgsConstructor
public class HomeConfigSubscriptionResponse {

    private Long id;

    private List<SubscriptionDTO> contents = new ArrayList<>();
}
