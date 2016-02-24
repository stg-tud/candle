package questionnaire.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import questionnaire.IOHelper;
import questionnaire.Result;
import questionnaire.CreationHelper;
import questionnaire.data.PlayerStatus;

public class QuestionServiceTest {

    private CreationHelper serverStatus;
    private IOHelper ioHelper;
    private QuestionService sut;
    private PlayerStatus player;

    @Before
    public void setup() {
        serverStatus = mock(CreationHelper.class);
        ioHelper = mock(IOHelper.class);
        player = mock(PlayerStatus.class);
        
        sut = new QuestionService(serverStatus, ioHelper);
    }

    @Test
    public void index() {
         assertNotNull(sut.index());
    }
    
    @Test
    public void register() {

        when(serverStatus.isValid(eq("abc"))).thenReturn(true);
        when(player.getName()).thenReturn("abc");
        when(serverStatus.createPlayer(eq("abc"))).thenReturn(player);
        when(ioHelper.exists(eq("abc"))).thenReturn(false);

        Result<String> actual = sut.register("abc");

        verify(serverStatus).isValid(eq("abc"));
        verify(ioHelper).exists(eq("abc"));
        verify(serverStatus).createPlayer(eq("abc"));

        Result<String> expected = Result.ok("abc");
        assertEquals(expected, actual);
    }
    //
    // @Test
    // public void registerPlayerExistsAlreadyTest() {
    // whenGetValid(status, auth);
    // Result<Void> answer = uut.register(auth);
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void registerSuccessfullyTest() {
    // whenGetValid(status);
    // Result<Void> answer = uut.register(auth);
    // assertEquals(OK, answer.status);
    // }
    //
    // @Test
    // public void firstPlayerDoesNotExistTest() {
    // whenGetValid(status);
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // Result<Task> answer = uut.first(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void firstActivePageIsWrongTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, "not the empty string");
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // Result<Task> answer = uut.first(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status).get(anyString());
    // verify(status).get(auth);
    // verify(player).getActivePage();
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void firstNoPagesTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, "");
    // Catalog catalog = mock(Catalog.class);
    // whenGetCatalog(player, catalog);
    // whenGetFirst(catalog, null);
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // Result<Task> answer = uut.first(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(2)).get(anyString());
    // verify(status, times(2)).get(auth);
    // verify(player).getActivePage();
    // verify(player).getCatalog();
    // verify(catalog).getFirst();
    // verify(player).setActivePage(anyString());
    // verify(player).setActivePage("study done");
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void firstSuccessTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, "");
    // Catalog catalog = mock(Catalog.class);
    // whenGetCatalog(player, catalog);
    // TaskWithSolution solution = mock(TaskWithSolution.class);
    // Task firstPage = mock(Task.class);
    // solution.page = firstPage;
    // firstPage.id = page;
    // whenGetFirst(catalog, solution);
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // Result<Task> answer = uut.first(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(2)).get(anyString());
    // verify(status, times(2)).get(auth);
    // verify(player).getActivePage();
    // verify(player).getCatalog();
    // verify(catalog).getFirst();
    // verify(player).setActivePage(anyString());
    // verify(player).setActivePage(page);
    // assertEquals(OK, answer.status);
    // assertEquals(firstPage, answer.data);
    // }
    //
    // @Test
    // public void nextPlayerDoesNotExistTest() {
    // whenGetValid(status);
    // Event event = mock(Event.class);
    // event.auth = auth;
    // Result<Task> answer = uut.next(event);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void nextActivePageIsNullTest() {
    // whenGetValid(status, auth);
    // Event event = mock(Event.class);
    // event.auth = auth;
    // event.pageID = null;
    // Result<Task> answer = uut.next(event);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(0)).get(anyString());
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void nextActivePageIsWrongTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, "wrong one");
    // Event event = mock(Event.class);
    // event.auth = auth;
    // event.pageID = page;
    // Result<Task> answer = uut.next(event);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status).get(anyString());
    // verify(status).get(auth);
    // verify(player).getActivePage();
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void nextNoNextTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, page);
    // Catalog catalog = mock(Catalog.class);
    // whenGetCatalog(player, catalog);
    // whenGetHasNext(catalog, page);
    // Event event = mock(Event.class);
    // event.auth = auth;
    // event.pageID = page;
    // Result<Task> answer = uut.next(event);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(2)).get(anyString());
    // verify(status, times(2)).get(auth);
    // verify(player).getActivePage();
    // verify(player).getCatalog();
    // verify(catalog).hasNext(anyString());
    // verify(catalog).hasNext(page);
    // verify(player).setActivePage(anyString());
    // verify(player).setActivePage("study done");
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void nextSomeNextTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, page);
    // Catalog catalog = mock(Catalog.class);
    // whenGetCatalog(player, catalog);
    // whenGetHasNext(catalog, "last");
    // TaskWithSolution solution = mock(TaskWithSolution.class);
    // Task nextPage = mock(Task.class);
    // solution.page = nextPage;
    // nextPage.id = "next";
    // whenGetNext(catalog, page, solution);
    // Event event = mock(Event.class);
    // event.auth = auth;
    // event.pageID = page;
    // Result<Task> answer = uut.next(event);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(2)).get(anyString());
    // verify(status, times(2)).get(auth);
    // verify(player).getActivePage();
    // verify(player).getCatalog();
    // verify(catalog).hasNext(anyString());
    // verify(catalog).hasNext(page);
    // verify(catalog).getNext(anyString());
    // verify(catalog).getNext(page);
    // verify(player).setActivePage(anyString());
    // verify(player).setActivePage("next");
    // assertEquals(OK, answer.status);
    // assertEquals(nextPage, answer.data);
    // }
    //
    // @Test
    // public void lastPlayerDoesNotExistTest() {
    // whenGetValid(status);
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // Result<Boolean> answer = uut.last(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void lastActivePageIsNullTest() {
    // whenGetValid(status, auth);
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // req.pageID = null;
    // Result<Boolean> answer = uut.last(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(0)).get(anyString());
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void lastActivePageIsWrongTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, "wrong one");
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // req.pageID = page;
    // Result<Boolean> answer = uut.last(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status).get(anyString());
    // verify(status).get(auth);
    // verify(player).getActivePage();
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void lastIsLastTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, page);
    // Catalog catalog = mock(Catalog.class);
    // whenGetCatalog(player, catalog);
    // whenGetHasNext(catalog, page);
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // req.pageID = page;
    // Result<Boolean> answer = uut.last(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(2)).get(anyString());
    // verify(status, times(2)).get(auth);
    // verify(player).getActivePage();
    // verify(player).getCatalog();
    // verify(catalog).hasNext(anyString());
    // verify(catalog).hasNext(page);
    // assertEquals(OK, answer.status);
    // assertEquals(true, answer.data);
    // }
    //
    // @Test
    // public void lastIsNotLastTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, page);
    // Catalog catalog = mock(Catalog.class);
    // whenGetCatalog(player, catalog);
    // whenGetHasNext(catalog, "last page");
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // req.pageID = page;
    // Result<Boolean> answer = uut.last(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(2)).get(anyString());
    // verify(status, times(2)).get(auth);
    // verify(player).getActivePage();
    // verify(player).getCatalog();
    // verify(catalog).hasNext(anyString());
    // verify(catalog).hasNext(page);
    // assertEquals(OK, answer.status);
    // assertEquals(false, answer.data);
    // }
    //
    // @Test
    // public void reloadPlayerDoesNotExistTest() {
    // whenGetValid(status);
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // Result<Task> answer = uut.reload(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void reloadActivePageIsNullTest() {
    // whenGetValid(status, auth);
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // req.pageID = null;
    // Result<Task> answer = uut.reload(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(0)).get(anyString());
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void reloadActivePageIsWrongTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, "wrong one");
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // req.pageID = page;
    // Result<Task> answer = uut.reload(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status).get(anyString());
    // verify(status).get(auth);
    // verify(player).getActivePage();
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void reloadSuccessTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, page);
    // Catalog catalog = mock(Catalog.class);
    // whenGetCatalog(player, catalog);
    // TaskWithSolution solution = mock(TaskWithSolution.class);
    // Task reloadedPage = mock(Task.class);
    // solution.page = reloadedPage;
    // reloadedPage.id = page;
    // whenGetReload(catalog, page, solution);
    // PageRequest req = mock(PageRequest.class);
    // req.auth = auth;
    // req.pageID = page;
    // Result<Task> answer = uut.reload(req);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(2)).get(anyString());
    // verify(status, times(2)).get(auth);
    // verify(player).getActivePage();
    // // verify(player).logEvent(any(Event.class));
    // verify(player).getCatalog();
    // verify(catalog).get(anyString());
    // verify(catalog).get(page);
    // assertEquals(OK, answer.status);
    // }
    //
    // @Test
    // public void eventPlayerDoesNotExistTest() {
    // whenGetValid(status);
    // Event event = mock(Event.class);
    // event.auth = auth;
    // Result<Void> answer = uut.event(event);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void eventActivePageIsNullTest() {
    // whenGetValid(status, auth);
    // Event event = mock(Event.class);
    // event.auth = auth;
    // event.pageID = null;
    // Result<Void> answer = uut.event(event);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status, times(0)).get(anyString());
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void eventActivePageIsWrongTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, "wrong one");
    // Event event = mock(Event.class);
    // event.auth = auth;
    // event.pageID = page;
    // Result<Void> answer = uut.event(event);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status).get(anyString());
    // verify(status).get(auth);
    // verify(player).getActivePage();
    // assertEquals(FAIL, answer.status);
    // }
    //
    // @Test
    // public void eventSuccessTest() {
    // whenGetValid(status, auth);
    // PlayerStatus player = mock(PlayerStatus.class);
    // whenGetPlayer(status, auth, player);
    // whenGetActivePage(player, page);
    // Event event = mock(Event.class);
    // event.auth = auth;
    // event.pageID = page;
    // Result<Void> answer = uut.event(event);
    // verify(status).isPlayerValid(anyString());
    // verify(status).isPlayerValid(auth);
    // verify(status).get(anyString());
    // verify(status).get(auth);
    // verify(player).getActivePage();
    // verify(status).log(any(Event.class));
    // verify(status).log(event);
    // assertEquals(OK, answer.status);
    // }
}