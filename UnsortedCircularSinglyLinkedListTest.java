package br.com.gomide.list.singly;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UnsortedCircularSinglyLinkedListTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(2);

  @Test
  @DisplayName("retrieve an empty list")
  public void retrieveAnEmptyList() {
    UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

    assertEquals("null", list.listContent());
    assertNull(list.getNextElement());
    assertNull(list.find(1));
    assertFalse(list.remove(1));
  }

  @Nested
  @DisplayName("insert action")
  class Insertion {

    @Test
    @DisplayName("insert elements at init successfully")
    public void insertElementsAtInitSuccessfully() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.prepend(2);
      list.prepend(5);
      list.prepend(8);
      list.prepend(4);

      assertEquals("4 -> 8 -> 5 -> 2 -> (back to 4)", list.listContent());
    }

    @Test
    @DisplayName("insert elements at end successfully")
    public void insertElementsAtEndSuccessfully() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(2);
      list.append(5);
      list.append(8);
      list.append(4);

      assertEquals("2 -> 5 -> 8 -> 4 -> (back to 2)", list.listContent());
    }

    @Test
    @DisplayName("insert elements successfully")
    public void insertElementsSuccessfully() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(2);
      list.prepend(5);
      list.prepend(8);
      list.append(4);

      assertEquals("8 -> 5 -> 2 -> 4 -> (back to 8)", list.listContent());
    }

    @Test
    @DisplayName("single element points to itself")
    public void singleElementPointsToItself() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(7);

      assertEquals("7 -> (back to 7)", list.listContent());
      assertEquals(7, list.getNextElement());
      assertEquals(7, list.getNextElement());
    }

    @Test
    @DisplayName("prepend keeps the last element linked to the new first")
    public void prependKeepsLastLinkedToNewFirst() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(2);
      list.append(5);
      list.prepend(9);
      list.clearNavigation();

      assertEquals(9, list.getNextElement());
      assertEquals(2, list.getNextElement());
      assertEquals(5, list.getNextElement());
      assertEquals(9, list.getNextElement());
    }

    @Test
    @DisplayName("append links the new last element to the first")
    public void appendLinksNewLastToFirst() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(2);
      list.append(5);
      list.append(8);

      assertEquals(2, list.getNextElement());
      assertEquals(5, list.getNextElement());
      assertEquals(8, list.getNextElement());
      assertEquals(2, list.getNextElement());
    }

    @Test
    @DisplayName("insert an array of elements")
    public void insertAnArrayOfElements() {
      Integer[] elements = new Integer[] { 2, 5, 8 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertEquals("2 -> 5 -> 8 -> (back to 2)", list.listContent());
    }

  }

  @Nested
  @DisplayName("find action")
  class Find {

    @Test
    @DisplayName("find an existing element")
    public void findAnExistingElement() {
      Integer[] elements = new Integer[] { 2, 5, 8, 4 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.prepend(elements);

      assertEquals(5, list.find(5));
    }

    @Test
    @DisplayName("find the first and the last elements")
    public void findTheFirstAndTheLastElements() {
      Integer[] elements = new Integer[] { 2, 5, 8, 4 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertEquals(2, list.find(2));
      assertEquals(4, list.find(4));
    }

    @Test
    @DisplayName("does not find an element and does not loop forever")
    public void doesNotFindAnElement() {
      Integer[] elements = new Integer[] { 2, 5, 8, 4 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.prepend(elements);

      assertTimeoutPreemptively(TIMEOUT, () -> {
        assertNull(list.find(666));
      });
    }

    @Test
    @DisplayName("does not find an element in a single element list")
    public void doesNotFindInSingleElementList() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(2);

      assertTimeoutPreemptively(TIMEOUT, () -> {
        assertNull(list.find(666));
      });
    }

  }

  @Nested
  @DisplayName("remove action")
  class Remove {

    @Test
    @DisplayName("remove an existing element")
    public void removeAnExistingElement() {
      Integer[] elements = new Integer[] { 2, 5, 8, 4 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertTrue(list.remove(5));
      assertEquals("2 -> 8 -> 4 -> (back to 2)", list.listContent());
    }

    @Test
    @DisplayName("remove the last element and keep the list circular")
    public void removeTheLastElement() {
      Integer[] elements = new Integer[] { 2, 5, 8, 4 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertTrue(list.remove(4));
      assertEquals("2 -> 5 -> 8 -> (back to 2)", list.listContent());

      // o novo último precisa apontar para o primeiro
      list.append(1);
      assertEquals("2 -> 5 -> 8 -> 1 -> (back to 2)", list.listContent());
    }

    @Test
    @DisplayName("remove the first element and keep the list circular")
    public void removeTheFirstElement() {
      Integer[] elements = new Integer[] { 2, 5, 8, 4 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertTrue(list.remove(2));
      assertEquals("5 -> 8 -> 4 -> (back to 5)", list.listContent());

      // o último precisa apontar para o novo primeiro
      list.append(7);
      assertEquals("5 -> 8 -> 4 -> 7 -> (back to 5)", list.listContent());
    }

    @Test
    @DisplayName("remove the first element of a single list")
    public void removeTheFirstElementOfASingleList() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(2);

      assertTrue(list.remove(2));
      assertEquals("null", list.listContent());
      assertNull(list.getNextElement());

      // a lista continua utilizável depois de esvaziada
      list.append(3);
      assertEquals("3 -> (back to 3)", list.listContent());
      assertEquals(3, list.getNextElement());
    }

    @Test
    @DisplayName("remove one element of a two elements list")
    public void removeOneElementOfATwoElementsList() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(2);
      list.append(5);

      assertTrue(list.remove(5));
      assertEquals("2 -> (back to 2)", list.listContent());
    }

    @Test
    @DisplayName("remove all elements one by one")
    public void removeAllElementsOneByOne() {
      Integer[] elements = new Integer[] { 2, 5, 8 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertTrue(list.remove(5));
      assertTrue(list.remove(2));
      assertTrue(list.remove(8));
      assertEquals("null", list.listContent());
      assertFalse(list.remove(8));
    }

    @Test
    @DisplayName("does not remove an element and does not loop forever")
    public void doesNotRemoveAnElement() {
      Integer[] elements = new Integer[] { 2, 5, 8, 4 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertTimeoutPreemptively(TIMEOUT, () -> {
        assertFalse(list.remove(666));
      });
      assertEquals("2 -> 5 -> 8 -> 4 -> (back to 2)", list.listContent());
    }

  }

  @Nested
  @DisplayName("navigation action")
  class Navigate {

    @Test
    @DisplayName("navigate through all elements and go back to the first one")
    public void navigateThroughAllElements() {
      Integer[] elements = new Integer[] { 2, 5, 8 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertEquals(2, list.getNextElement());
      assertEquals(5, list.getNextElement());
      assertEquals(8, list.getNextElement());
      assertEquals(2, list.getNextElement());
    }

    @Test
    @DisplayName("navigate through the list several times")
    public void navigateThroughTheListSeveralTimes() {
      Integer[] elements = new Integer[] { 2, 5, 8 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      for (int lap = 0; lap < 3; lap++) {
        assertEquals(2, list.getNextElement());
        assertEquals(5, list.getNextElement());
        assertEquals(8, list.getNextElement());
      }
    }

    @Test
    @DisplayName("navigate through elements and restart navigation pointer")
    public void navigateThroughElementsAndRestartNavigationPointer() {
      Integer[] elements = new Integer[] { 2, 5, 8 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertEquals(2, list.getNextElement());
      assertEquals(5, list.getNextElement());

      list.clearNavigation();

      assertEquals(2, list.getNextElement());
      assertEquals(5, list.getNextElement());
      assertEquals(8, list.getNextElement());
      assertEquals(2, list.getNextElement());
    }

    @Test
    @DisplayName("navigation moves on when the current element is removed")
    public void navigationMovesOnWhenCurrentElementIsRemoved() {
      Integer[] elements = new Integer[] { 2, 5, 8 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      assertEquals(2, list.getNextElement()); // ponteiro agora está no 5

      list.remove(5);

      assertEquals(8, list.getNextElement());
      assertEquals(2, list.getNextElement());
    }

    @Test
    @DisplayName("navigation goes back to the first element when the last one is removed")
    public void navigationGoesBackToFirstWhenLastIsRemoved() {
      Integer[] elements = new Integer[] { 2, 5, 8 };
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(elements);

      list.getNextElement();
      list.getNextElement(); // ponteiro agora está no 8 (último)

      list.remove(8);

      assertEquals(2, list.getNextElement());
      assertEquals(5, list.getNextElement());
      assertEquals(2, list.getNextElement());
    }

    @Test
    @DisplayName("navigate through a single element list")
    public void navigateThroughASingleElementList() {
      UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

      list.append(2);

      assertEquals(2, list.getNextElement());
      assertEquals(2, list.getNextElement());
      assertEquals(2, list.getNextElement());
    }

  }

  @Test
  @DisplayName("clear a list")
  public void clearAListWithElements() {
    Integer[] elements = new Integer[] { 2, 5, 8, 4 };
    UnsortedCircularSinglyLinkedList<Integer> list = new UnsortedCircularSinglyLinkedList<Integer>();

    list.append(elements);

    list.clear();

    assertEquals("null", list.listContent());
    assertNull(list.getNextElement());
  }

}
