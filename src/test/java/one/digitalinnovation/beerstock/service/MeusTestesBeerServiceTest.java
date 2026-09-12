package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.dto.BeerDTO;
import one.digitalinnovation.beerstock.entity.Beer;
import one.digitalinnovation.beerstock.enums.BeerType;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;
import one.digitalinnovation.beerstock.exception.BeerStockExceededException;
import one.digitalinnovation.beerstock.mapper.BeerMapper;
import one.digitalinnovation.beerstock.repository.BeerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Meus testes adicionais - aprendendo QA na prática")
class MeusTestesBeerServiceTest {

    private static final long VALID_BEER_ID = 1L;
    private static final String BEER_NAME = "Brahma";
    private static final String BEER_BRAND = "Ambev";
    private static final int MAX_QUANTITY = 50;
    private static final int INITIAL_QUANTITY = 10;

    @Mock
    private BeerRepository beerRepository;

    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;

    @Test
    @DisplayName("Nao deve encontrar cerveja com nome inexistente")
    void whenInvalidNameThenThrowException() {
        when(beerRepository.findByName("Inexistente")).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class,
                () -> beerService.findByName("Inexistente"));
    }

    @Test
    @DisplayName("Deve listar todas as cervejas cadastradas")
    void whenListAllThenReturnList() {
        Beer beer = new Beer(VALID_BEER_ID, BEER_NAME, BEER_BRAND, MAX_QUANTITY, INITIAL_QUANTITY, BeerType.LAGER);
        when(beerRepository.findAll()).thenReturn(Collections.singletonList(beer));

        List<BeerDTO> beers = beerService.listAll();

        assertFalse(beers.isEmpty());
        assertEquals(1, beers.size());
        assertEquals(BEER_NAME, beers.get(0).getName());
    }

    @Test
    @DisplayName("Deve validar que o repositorio foi chamado ao buscar por nome")
    void whenFindByNameThenVerifyRepositoryCall() throws BeerNotFoundException {
        Beer beer = new Beer(VALID_BEER_ID, BEER_NAME, BEER_BRAND, MAX_QUANTITY, INITIAL_QUANTITY, BeerType.LAGER);
        when(beerRepository.findByName(BEER_NAME)).thenReturn(Optional.of(beer));

        BeerDTO found = beerService.findByName(BEER_NAME);

        assertNotNull(found);
        assertEquals(BEER_NAME, found.getName());
        verify(beerRepository, times(1)).findByName(BEER_NAME);
    }

    @Test
    @DisplayName("Deve lancar excecao ao cadastrar cerveja ja existente")
    void whenAlreadyRegisteredThenThrowException() {
        BeerDTO dto = new BeerDTO();
        dto.setName(BEER_NAME);

        Beer beer = new Beer(VALID_BEER_ID, BEER_NAME, BEER_BRAND, MAX_QUANTITY, INITIAL_QUANTITY, BeerType.LAGER);
        when(beerRepository.findByName(BEER_NAME)).thenReturn(Optional.of(beer));

        assertThrows(BeerAlreadyRegisteredException.class,
                () -> beerService.createBeer(dto));
    }

    @Test
    @DisplayName("Deve incrementar o estoque de cerveja com sucesso")
    void whenIncrementThenReturnUpdatedBeer() throws BeerNotFoundException, BeerStockExceededException {
        Beer beer = new Beer(VALID_BEER_ID, BEER_NAME, BEER_BRAND, MAX_QUANTITY, INITIAL_QUANTITY, BeerType.LAGER);
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(beer));
        when(beerRepository.save(any(Beer.class))).thenReturn(beer);

        BeerDTO result = beerService.increment(VALID_BEER_ID, 5);

        assertNotNull(result);
        assertEquals(INITIAL_QUANTITY + 5, result.getQuantity());
    }

    @Test
    @DisplayName("Deve lancar excecao ao incrementar estoque acima do maximo")
    void whenIncrementExceedsMaxThenThrowException() {
        Beer beer = new Beer(VALID_BEER_ID, BEER_NAME, BEER_BRAND, MAX_QUANTITY, MAX_QUANTITY, BeerType.LAGER);
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(beer));

        assertThrows(BeerStockExceededException.class,
                () -> beerService.increment(VALID_BEER_ID, 5));
    }
}