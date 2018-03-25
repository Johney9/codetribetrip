package trip.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import trip.Application;
import trip.model.Trip;
import trip.repository.TripRepository;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
public class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private Trip trip = null;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private MultiValueMap<String, String> sortDemo;

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.trip = this.tripRepository.save(new Trip("Beograd", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 999999999), ""));
        this.sortDemo = new LinkedMultiValueMap<String, String>();
        this.sortDemo.put("destination", testDestinations());
        this.sortDemo.put("startDate", testStartDates());
        this.sortDemo.put("endDate", testEndDates());
    }

    private List<String> testEndDates() {
        List<String> list = new ArrayList<>();
        list.add("2018-03-10");
        list.add("2018-04-22");
        list.add("2018-09-10");
        list.add("2018-05-30");
        return list;
    }

    private List<String> testStartDates() {
        List<String> list = new ArrayList<>();
        list.add("2018-03-02");
        list.add("2018-04-20");
        list.add("2018-07-10");
        list.add("2018-05-11");
        return list;
    }

    private List<String> testDestinations() {
        List<String> list = new ArrayList<>();
        list.add("Paris");
        list.add("Novi Sad");
        list.add("Hawaii");
        list.add("Chicago");
        return list;
    }

    @Test
    public void tripReadSuccess() throws Exception {
        mockMvc.perform(get("/trip/" + this.trip.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createTrip() throws Exception {
        mockMvc.perform(post("/trip/create-trip")
                .param("destination", "Novi Sad")
                .param("startDate", "2018-04-15")
                .param("endDate", "2018-04-16")
                .param("comment", "short")).andExpect(status().isCreated());

    }

    @Test
    public void showFutureTripsStartingBefore() throws Exception {
        mockMvc.perform(get("/trip/show-future-trips-starting-before")
                .param("startDate", "2018-4-30")).andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void showFutureTripsStartingBeforeSortingInc() throws Exception {
        List<String> destinations = testDestinations();
        List<String> startDates = testStartDates();
        List<String> endDates = testEndDates();
        for (int i = 0; i < destinations.size(); i++) {
            mockMvc.perform(post("/trip/create-trip")
                    .param("destination", destinations.get(i))
                    .param("startDate", startDates.get(i))
                    .param("endDate", endDates.get(i)))
                    .andExpect(status().isCreated());
        }
        mockMvc.perform(get("/trip/show-future-trips-starting-before")
                .param("startDate", "2018-09-09")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destination", is("Novi Sad")))
                .andExpect(jsonPath("$[2].destination", is("Chicago")))
                .andExpect(jsonPath("$[3].destination", is("Hawaii")));
    }

    @Test
    public void searchTrips() throws Exception {
        mockMvc.perform(get("/trip/search").param("destination", "Beograd")).andExpect(jsonPath("$", hasSize(2)));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}