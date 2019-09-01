import React from 'react';

import axios from 'axios';

export default class App extends React.Component {
    static updateContact = (event, id, contacts) => {
      event.preventDefault();
      this.setRowToReadOnly(id);
      const data = {
        name: document.getElementById(`${id}-name`).value,
        phoneNumber: document.getElementById(`${id}-phoneNumber`).value,
        emailAddress: document.getElementById(`${id}-emailAddress`).value,
      };
      axios.post(`http://localhost:8080/contacts/${id}`, data).then((response) => {
        const index = contacts.indexOf(data);
        const contactsCopy = contacts;
        contactsCopy[index] = response.data;
        if (index !== -1) {
          this.setState({
            contacts: contactsCopy,
          });
        }
      }).catch(() => { App.cancelAction(id, contacts); });
    }

    static cancelAction(id, contacts) {
      this.setRowToReadOnly(id);
      const origValue = contacts.find((c) => c.id === id);

      document.getElementById(`${id}-name`).value = origValue.name;
      document.getElementById(`${id}-phoneNumber`).value = origValue.phoneNumber;
      document.getElementById(`${id}-emailAddress`).value = origValue.emailAddress;
    }

    static getSaveButton(id, contacts) {
      return (
        <button
          name="save"
          value="save"
          id={`${id}-save-button`}
          hidden="hidden"
          type="submit"
          onClick={(event) => App.updateContact(event, id, contacts)}
        >
                save
        </button>
      );
    }

    static setRowToReadOnly(id) {
      document.getElementById(`${id}-name`).readOnly = true;
      document.getElementById(`${id}-phoneNumber`).readOnly = true;
      document.getElementById(`${id}-emailAddress`).readOnly = true;
      document.getElementById(`${id}-save-button`).hidden = true;
      document.getElementById(`${id}-delete-button`).hidden = true;
      document.getElementById(`${id}-cancel-edit-button`).hidden = true;
      Array.from(document.getElementsByClassName('edit-button'))
        .forEach((cb) => {
          const a = cb;
          a.hidden = false;
        });
    }


    constructor(props) {
      super(props);
      this.state = {
        contacts: [],
      };
    }

    componentDidMount() {
      axios.get('http://localhost:8080/contacts')
        .then((res) => {
          const contacts = res.data;
          this.setState({ contacts });
        });
    }


    static getEditButton(id) {
      return (
        <button
          name="edit"
          value="edit"
          id={`${id}-edit-button`}
          className="edit-button"
          type="button"
          onClick={
                           () => {
                             document.getElementById(`${id}-name`).readOnly = false;
                             document.getElementById(`${id}-phoneNumber`).readOnly = false;
                             document.getElementById(`${id}-emailAddress`).readOnly = false;
                             document.getElementById(`${id}-save-button`).hidden = false;
                             document.getElementById(`${id}-delete-button`).hidden = false;
                             document.getElementById(`${id}-cancel-edit-button`).hidden = false;
                             Array.from(document.getElementsByClassName('edit-button'))
                               .forEach((cb) => { const a = cb; a.hidden = true; });
                           }
                       }
        >
edit
        </button>
      );
    }

    static getCancelEditButton(id, contacts) {
      return (
        <button
          name="cancel edit"
          value="cancel edit"
          id={`${id}-cancel-edit-button`}
          hidden="hidden"
          type="button"
          onClick={
                           () => {
                             this.cancelAction(id, contacts);
                           }
                       }
        >
cancel edit
        </button>
      );
    }

    getDeleteButton(id, contacts) {
      return (
        <button
          name="delete"
          value="delete"
          id={`${id}-delete-button`}
          hidden="hidden"
          type="button"
          onClick={(event) => this.deleteContact(event, id, contacts)}
        >
delete
        </button>
      );
    }

    deleteContact = (event, id, contacts) => {
      event.preventDefault();
      Array.from(document.getElementsByClassName('edit-button'))
        .forEach((cb) => { const a = cb; a.hidden = false; });
      axios.delete(`http://localhost:8080/contacts/${id}`).then(() => {
        document.getElementById(`${id}-tr`).remove();
        const index = contacts.findIndex((a) => a.id === id);
        const contactsCopy = contacts;
        contactsCopy.slice(index, 1);
        if (index !== -1) {
          this.setState({
            contacts: contactsCopy,
          });
        }
      }).catch(() => { App.cancelAction(id, contacts); });
    }

    handleSubmit = (event, origContacts) => {
      event.preventDefault();
      const form = new FormData(event.target);
      const data = {
        name: form.get('name'),
        phoneNumber: form.get('phoneNumber'),
        emailAddress: form.get('emailAddress'),
      };
      axios.post('http://localhost:8080/contacts', data).then((response) => {
        document.getElementById('error-div').hidden = true;

        const modC = origContacts.concat([
          (response.data)]);
        this.setState({
          contacts: modC,
        });
      }).catch(() => {
        document.getElementById('error-div').innerText = 'Error, could not upload new contact';
        document.getElementById('error-div').hidden = false;
      });
    }


    render() {
      const { contacts } = this.state;

      function addContact() {
        return (
          <div>
            <h2>Add Contact</h2>
            <div id="error-div" hidden="hidden" />
            <form onSubmit={(event) => this.handleSubmit(event, contacts)}>
              <label htmlFor="name">
Name*:
                <input type="text" name="name" id="name" defaultValue="" required />
              </label>
              <br />
              <label htmlFor="phoneNumber">
PhoneNumber*:
                <input type="text" name="phoneNumber" id="phoneNumber" pattern="^[+]36\d{9}$" title="phone number must be a valid hungarian phone number" defaultValue="" required />
              </label>
              <br />
              <label htmlFor="emailAddress">
EmailAddress*:
                <input type="email" name="emailAddress" id="emailAddress" defaultValue="" required />
              </label>
              <br />
              <button type="submit">Add new user</button>
            </form>
          </div>
        );
      }

      function viewContacts() {
        return (
          <div>
            <h2>View users</h2>
            <table>
              <tbody>
                <tr>
                  <th>name</th>
                  <th>phoneNumber</th>
                  <th>emailAddress</th>
                </tr>
                {contacts.map((contact) => (
                  <tr id={`${contact.id}-tr`} key={`${contact.id}-key`}>
                    <td>
                      <input
                        id={`${contact.id}-name`}
                        type="text"
                        name="name"
                        defaultValue={contact.name}
                        readOnly="readOnly"
                      />
                    </td>
                    <td>
                      <input
                        id={`${contact.id}-phoneNumber`}
                        type="text"
                        name="phoneNumber"
                        defaultValue={contact.phoneNumber}
                        readOnly="readOnly"
                      />
                    </td>
                    <td>
                      <input
                        id={`${contact.id}-emailAddress`}
                        type="email"
                        name="emailAddress"
                        defaultValue={contact.emailAddress}
                        readOnly="readOnly"
                      />
                    </td>
                    <td>{App.getEditButton(contact.id)}</td>
                    <td>{App.getCancelEditButton(contact.id, contacts)}</td>
                    <td>{App.getSaveButton(contact.id, contacts)}</td>
                    <td>{this.getDeleteButton(contact.id, contacts)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        );
      }

      return (
        <div>
          <h1>Contact list App</h1>
          <div>
            {addContact.call(this)}
            {viewContacts.call(this)}
          </div>
        </div>
      );
    }
}
