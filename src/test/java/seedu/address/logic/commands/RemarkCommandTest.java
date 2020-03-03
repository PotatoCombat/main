package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for {@code RemarkCommand}.
 */
class RemarkCommandTest {
    private static final String REMARK_STUB = "Changed remark";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    void execute_addRemarkUnfilteredList_success() {
        Person personToRemark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToRemark).withRemark(REMARK_STUB).build();

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(editedPerson.getRemark().value));

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, editedPerson);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToRemark, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    void execute_deleteRemarkUnfilteredList_success() {
        Person personToRemark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToRemark).withRemark("").build();

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(editedPerson.getRemark().value));

        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS, editedPerson);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToRemark, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToRemark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withRemark(REMARK_STUB).build();

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(editedPerson.getRemark().value));

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, editedPerson);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToRemark, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    void execute_invalidPersonIndexUnfilteredList_failure() {
        Index unknownIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 10);

        RemarkCommand remarkCommand = new RemarkCommand(unknownIndex, new Remark(VALID_REMARK_AMY));

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index unknownIndex = INDEX_THIRD_PERSON;

        assertTrue(unknownIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        RemarkCommand remarkCommand = new RemarkCommand(unknownIndex, new Remark(VALID_REMARK_AMY));

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
