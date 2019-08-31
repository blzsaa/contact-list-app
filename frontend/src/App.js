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
      });
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
                             this.setRowToReadOnly(id);
                             const origValue = contacts.find((c) => c.id === id);

                             document.getElementById(`${id}-name`).value = origValue.name;
                             document.getElementById(`${id}-phoneNumber`).value = origValue.phoneNumber;
                             document.getElementById(`${id}-emailAddress`).value = origValue.emailAddress;
                           }
                       }
        >
cancel edit
        </button>
      );
    }

    static getSaveButton(id, contacts) {
      return (
        <button
          name="save"
          value="save"
          id={`${id}-save-button`}
          hidden="hidden"
          type="button"
          onClick={(event) => App.updateContact(event, id, contacts)}
        >
save
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
      });
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
        const modC = origContacts.concat([
          (response.data)]);
        this.setState({
          contacts: modC,
        });
      });
    }


    render() {
      const { contacts } = this.state;

      function addContact() {
        return (
          <div>
            <h2>Add Contact</h2>
            <form onSubmit={(event) => this.handleSubmit(event, contacts)}>
              <label htmlFor="name">
Name
                <input type="text" name="name" id="name" defaultValue="" />
              </label>
              <label htmlFor="phoneNumber">
phoneNumber
                <input type="text" name="phoneNumber" id="phoneNumber" defaultValue="" />
              </label>
              <label htmlFor="emailAddress">
emailAddress
                <input type="text" name="emailAddress" id="emailAddress" defaultValue="" />
              </label>
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
                  <tr id={`${contact.id}-tr`}>
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
                        type="text"
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
