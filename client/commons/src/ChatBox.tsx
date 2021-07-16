import React from 'react';
// import {createApiClient, Order} from './api';
import {addResponseMessage, setQuickButtons, Widget} from 'react-chat-widget';
import { createApiClient } from './api'

interface Props {
  isRep: boolean,
  customerUrl: string,
  email: string,
  session: string
}

interface ChatBoxComponentState {
  nextMessageNumber:number
}

const api = createApiClient();
const buttons = [{label: 'first', value: '1'}, {label: 'second', value: '2'}];

export class ChatBox extends React.Component<Props, ChatBoxComponentState> {
  public constructor(props: Props) {
    super(props);
    this.state = {
      nextMessageNumber:1
    }
    // this.handleClicked = this.handleClicked.bind(this);
    // this.toggleClicked = this.toggleClicked.bind(this);
  }


  async componentDidMount() {
    addResponseMessage('Welcome to this awesome chat!')
    // setQuickButtons(buttons);
  }

  handleNewUserMessage = async(newMessage: string) => {
    console.log(`New message incoming! ${newMessage}`)
    // Now send the message through the backend API
    await api.sendMessage(newMessage,this.props.session, this.props.isRep, this.state.nextMessageNumber,false );
    await this.setState({
      nextMessageNumber:this.state.nextMessageNumber+1
    })
  }

  handleQuickButtonClicked = (data: string) => {
    console.log(data)
    setQuickButtons(buttons.filter((button) => button.value !== data))
  }

  // render is only called once we are already logged in - so we need to start listening to messages/changes

  render() {
    return (
      // <div className="App">
      <Widget
        handleNewUserMessage={this.handleNewUserMessage}
        // handleQuickButtonClicked={this.handleQuickButtonClicked}
        // profileAvatar={'text'}
        title='Polls'
        subtitle='Polls Demo'
      />
      // </div>
    )
  }

  // async handleChangeDeliveryStatus(order: Order) {
  //     const newFulfillmentStatus = (order.fulfillmentStatus === 'fulfilled') ? 'not-fulfilled' : 'fulfilled';
  //     await api.changeOrderDeliveryStatus(order.id, newFulfillmentStatus);
  //     order.fulfillmentStatus = newFulfillmentStatus;
  //     this.setState({
  //         order: order
  //     })
  // }
  // handleClicked() {
  //     this.setState(this.toggleClicked);
  // }

  // toggleClicked(state: { displayingItems: boolean }) {
  //     let newText = (state.displayingItems) ? 'Show items' : 'Hide items';
  //     return {
  //         order: this.state.order,
  //         displayedText: newText,
  //         displayingItems: !state.displayingItems,
  //     };
  // }
  // static getAssetByStatus(status: string) {
  //     switch (status) {
  //         case 'fulfilled':
  //             return require('./assets/package.png');
  //         case 'not-fulfilled':
  //             return require('./assets/pending.png');
  //         case 'canceled':
  //             return require('./assets/cancel.png');
  //         case 'paid':
  //             return require('./assets/paid.png');
  //         case 'not-paid':
  //             return require('./assets/not-paid.png');
  //         case 'refunded':
  //             return require('./assets/refunded.png');
  //     }
  // }
}
