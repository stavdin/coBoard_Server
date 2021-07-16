
import axios from 'axios';

export type Customer = {
  name: string;
}

export type SendMessageResponse = {
  messageSent:true;
}
export type CreateSessionResponse = {
  sessionId:string;
}
export type EndSessionResponse = {
  sessionId:string;
}
export type Message = {
  text:string;
  isTerminationMessage:boolean;
  messageNum:number;
  isRep:boolean;
}
export type WaitForMessageResponse = {
  syncPoint:number;
  messages: Message[];
}



export type ApiClient = {
  createSession: (customer: string, email: string) => Promise<string>;
  // give customer string and email of user, return id of created session


  // endSession: (session:string, isRep:boolean, messageNum:number) => Promise<string>;
  // // give session string and isRep, return id of ended session
  //
  //
  sendMessage: (text:string, session:string, isRep:boolean, messageNum:number, isTerminationMessage:boolean) => Promise<boolean>;
  //  give session string, is rep, session number and if this is the last message. returns a boolean that says if the message was received


  // waitForMessage: (session:string, time:string, isRep:boolean, syncPoint:number) => Promise<WaitForMessageResponse>;


  // getOrderCount: (searchText: string, deliveryFilter: string, paymentFilter: string) => Promise<number>;
  // listenToChanges: (syncPoint: number) => Promise<WaitForOrderChangesResponse>;
}


  export const createApiClient = (): ApiClient => {
    return {
      // endSession(session: string, isRep: boolean, messageNum: number): Promise<string> {
      //   return axios.get(`http://localhost:8080/endSession`, {
      //     params: {
      //       sid: session,
      //       isRep: isRep,
      //       messageNum: messageNum
      //     }
      //   })
      // },
      sendMessage(text:string, session: string, isRep: boolean, messageNum: number, isTerminationMessage: boolean): Promise<boolean> {
        return axios.post(`http://localhost:8080/sendMessage`,{
          params: {
            text:text,
            sid:session,
            isRep:isRep,
            messageNum:messageNum,
            isTerminationMessage:isTerminationMessage
          }
        });
      },
      // waitForMessage(session: string, time: string, isRep: boolean, syncPoint: number): Promise<WaitForMessageResponse> {
      //   return axios.get(`http://localhost:8080/waitForMessage`,{
      //     params: {
      //       sid:session,
      //       time:time,
      //       isRep:isRep,
      //       syncpoint:syncPoint
      //     }
      //   });
      // },
      createSession: (customer:string, email:string) => {
        return axios.get(`http://localhost:8080/createSession`, {
          params: {
            cid:customer,
            email:email
          }
        });
      }
    }
  }

// export const createApiClient = (): ApiClient => {
//   return {
//     getOrders: (searchText: string, page: number, deliveryFilter: string, paymentFilter: string) => {
//       return axios.get(`http://localhost:3232/api/orders`, {
//         params: {
//           page: page,
//           searchText: searchText,
//           deliveryFilter: deliveryFilter,
//           paymentFilter: paymentFilter
//         }
//       }).then((res) => res.data);
//     },
//     getItem: (itemId: string) => {
//       return axios.get(`http://localhost:3232/api/items/${itemId}`).then((res) => res.data);
//     },
//     changeOrderDeliveryStatus: (orderId: number, deliveryStatus: string) => {
//       return axios.post(`http://localhost:3232/api/orders/${orderId}/changeOrderDeliveryStatus`, {
//         deliveryStatus: deliveryStatus
//       }).then((res) => res.data);
//     },
//     getOrderLines: (orderId: number) => {
//       return axios.get(`http://localhost:3232/api/orders/${orderId}/getOrderLines`).then((res) => res.data);
//     },
//     getOrderCount: (searchText: string, deliveryFilter: string, paymentFilter: string) => {
//       return axios.get(`http://localhost:3232/api/orders/getOrderCount`, {
//         params: {
//           searchText: searchText,
//           deliveryFilter: deliveryFilter,
//           paymentFilter: paymentFilter
//         }
//       }).then((res) => res.data.length)
//     },
//     listenToChanges: (syncPoint) => {
//       return axios.get(`http://localhost:3232/api/listenToChanges`, {
//         params: {
//           syncPoint: syncPoint
//         }
//       }).then((res) => res.data);
//     }
//   }
// }

