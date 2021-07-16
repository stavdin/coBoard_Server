import React from 'react'
import './styles.css'
//import {Widget, addResponseMessage, addLinkSnippet, addUserMessage, setQuickButtons} from 'react-chat-widget';
//import {createApiClient, Order} from './api';
import { LoginForm, ChatBox, createApiClient } from 'commons'
//import {ChatBox} from 'commons/dist/ChatBox';
import 'react-chat-widget/lib/styles.css'
// import {LoginForm} from "./LoginForm"
//import {ChatBox} from "./ChatBox";

// import {FormControl, FormLabel, RadioGroup, FormControlLabel, Radio, Box} from '@material-ui/core'
// import {MuiThemeProvider, createMuiTheme} from '@material-ui/core/styles';
// const theme = createMuiTheme({
//     palette: {
//         secondary: {main: '#d71111'},
//         primary: {main: '#12e91c'},
//     },
// });

export type AppState = {
  syncPoint: number,
  loggedIn: boolean,
  customerUrl: string
  email?: string
  isRep?: boolean
  session?: string
}

const api = createApiClient()

export class App extends React.PureComponent<{}, AppState> {

  state: AppState = {
    syncPoint: 0,
    loggedIn: false,
    customerUrl: window.location.href
  }
  started: boolean = false


  // async initClient() {
  //     let syncPoint: number = 0;
  //     while (true) {
  //         if (!this.state.orders) {
  //             await this.wait(2000);
  //         } else {
  //             try {
  //                 let waitForOrderChangesResponse = await api.listenToChanges(syncPoint); // we wait for changes
  //                 let changedOrders = waitForOrderChangesResponse.changedOrders;
  //                 let notDeliveredCount = waitForOrderChangesResponse.notDeliveredCount;
  //                 syncPoint = waitForOrderChangesResponse.syncPoint;
  //                 let newOrdersArray = [...this.state.orders];
  //                 for (let order of changedOrders) {
  //                     for (let newOrder of newOrdersArray) {
  //                         if (newOrder.id === order.id) {
  //                             newOrder.fulfillmentStatus = order.fulfillmentStatus;
  //                         }
  //                     }
  //                 }
  //                 this.setState({
  //                     orders: newOrdersArray,
  //                     totalNotDeliveredOrders: notDeliveredCount
  //                 });
  //             } catch (err) {
  //                 console.log(err)
  //                 await this.wait(2000);
  //             }
  //         }
  //     }
  // }

  async logIn() {
  }

  onLogin = async (email: string, isRep: boolean) => {
    this.setState({
      isRep: isRep,
      loggedIn: true,
      email: email
    })
    let session = await api.createSession(this.state.customerUrl, email)
    await (this.setState({
      session: session
    }))
    console.log('session created')
  }

  render() {
    if (!this.state.loggedIn) {
      return (
        <div className='LoginComp'>
          <LoginForm customerUrl={this.state.customerUrl} onLogin={this.onLogin} passRequired={false} />
        </div>
      )
    } else {
      return (
        <ChatBox customerUrl={this.state.customerUrl} email={(this.state.email) ? this.state.email : ''}
                 session={(this.state.session) ? this.state.session : ''} isRep={false} />
      )
    }
  }
}

export default App
