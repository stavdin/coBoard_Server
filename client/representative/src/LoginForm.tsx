import React from 'react';
import {createApiClient, Order, OrderLine} from './api';
import 'reactjs-popup/dist/index.css';
import Popup from "reactjs-popup";
import './styles.css';


interface IProps {
    customerUrl: string
    onLogin: (any)
    passRequired:boolean
}

interface IState {
    email: string,
    password: string,
}

//const api = createApiClient();

export class LoginForm extends React.Component<IProps, IState> {
    public constructor(props: IProps) {
        super(props);
        this.state = {
            email: '',
            password: '',
        }
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleEmailChange(event: any) {
        this.setState({email: event.target.email});
    }

    handlePasswordChange(event: any) {
        this.setState({password: event.target.password});
    }

    async handleSubmit(event: any) {
        if (this.state.password !== '') {
            // await ask server for log in authentication
            //if authentication fails return here - use alert to display wrong login input and clear fields
            //setstate isRep true after authentication succeeds
            //let parent component know we are logged in
            this.props.onLogin(this.state.email, true);
        } else {
            // check email validity
            // let parent component know we are logged in
            this.props.onLogin(this.state.email, false);

            // alert('submitted: ' + this.state.email + this.state.password);
            // event.preventDefault();
        }
    }


    render() {
        if(this.props.passRequired){
            return (
                <div>
                    <Popup trigger={<button>Start chat</button>} position='left bottom'>
                        <div className="header"> Representative Login </div>
                        <br/>
                        <div>
                            <div>
                                <label>
                                    E-mail: <span>&nbsp;&nbsp;</span>
                                    <input type="text" value={this.state.email} onChange={this.handleEmailChange}/>
                                </label>
                            </div>
                            <div>
                                <label>
                                    Password (reps only): <span>&nbsp;&nbsp;</span>
                                    <input type="text" value={this.state.password} onChange={this.handlePasswordChange}/>
                                </label>
                            </div>
                            <div>
                                <button className={'submit'} onClick={this.handleSubmit}>Submit</button>
                            </div>
                        </div>
                    </Popup>
                </div>
            );
        }

        else{
            return(
                <div>
                    <Popup trigger={<button>Start chat</button>} position='left bottom'>
                        <div className="header"> Insert email to start chatting </div>
                        <br/>
                        <div>
                            <div>
                                <label>
                                    E-mail: <span>&nbsp;&nbsp;</span>
                                    <input type="text" value={this.state.email} onChange={this.handleEmailChange}/>
                                </label>
                            </div>
                            <div>
                                <button className={'submit'} onClick={this.handleSubmit}>Submit</button>
                            </div>
                        </div>
                    </Popup>
                </div>
            );
        }

    }

}


