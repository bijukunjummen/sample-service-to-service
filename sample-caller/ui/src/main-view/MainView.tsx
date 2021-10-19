import React, { useState } from "react";
import * as restCalls from "./client-calls";
import { MessageAck } from "./MessageAck";

export const FormErrors: React.FunctionComponent<{ formErrors: Record<string, string> }> = ({ formErrors }) => (
  <div>
    {Object.keys(formErrors).map((fieldName, i) => {
      if (formErrors[fieldName].length > 0) {
        return (
          <p key={fieldName} className="alert alert-warning">{fieldName} {formErrors[fieldName]}</p>
        )
      } else {
        return '';
      }
    })}
  </div>
)

export const ProgressBar = ({ loading }: { loading: boolean }) =>
  <div className="row">
    <div className="col-sm-2">
    </div>
    <div className="col-sm-8">
      {loading &&
        <div className="progress">
          <div className="progress-bar progress-bar-striped progress-bar-animated w-100" role="progressbar"
            aria-valuenow={100}
            aria-valuemin={0}
            aria-valuemax={100}></div>
        </div>
      }
      <div className="col-sm-2">
      </div>
    </div>
  </div>

export const ResponseDisplay = ({ responseMessage, responseError }: { responseMessage?: MessageAck, responseError: string }) => {
  return <div className="row mt-3">
    <div className="col-sm-12">
      {responseMessage &&
        <div className="alert alert-success">
          <div className="row">
            <label htmlFor="id" className="col-sm-2">Id : </label>
            <span className="col-sm-4">{responseMessage.id}</span>
          </div>
          <div className="row">
            <label htmlFor="message" className="col-sm-2">Recieved : </label>
            <span className="col-sm-4">{responseMessage.received}</span>
          </div>
          <div className="row">
            <label htmlFor="acked" className="col-sm-2">Acked : </label>
            <span className="col-sm-4">{responseMessage.ack}</span>
          </div>
        </div>
      }

      {responseError &&
        <div className="alert alert-warning">
          <div className="row">
            <span className="col-sm-4">{responseError}</span>
          </div>
        </div>
      }
    </div>
  </div>
};

interface CallState {
    payload: string;
    delay: number;
    formErrors: { payload: string, delay: string };
    loading: boolean;
    payloadValid: boolean;
    delayValid: boolean;
    formValid: boolean;
    responseMessage?: MessageAck;
    responseError: string;
}

export const MainForm = ({payload}: {payload: string}) => {
  const [callState, setCallState] = useState<CallState>({
    payload: "dummy payload",
    delay: 100,
    formErrors: { payload: '', delay: '' },
    loading: false,
    payloadValid: true,
    delayValid: true,
    formValid: true,
    responseMessage: undefined,
    responseError: ""
  });

  const handleFormSubmit = (e:React.FormEvent<HTMLFormElement>) => {
    if (callState.loading) {
      e.preventDefault();
      return;
    }
    setCallState(prevState => ({...prevState, responseError: "" }));
    setCallState(prevState => ({...prevState, responseMessage: undefined }));

    passthroughCallAndSetState(callState.payload, callState.delay);
    e.preventDefault()
  }

  const passthroughCallAndSetState = (payload: string, delay: number) => {
    setCallState(prevState => ({ ...prevState, loading: true }));
    restCalls
      .makePassthroughCall(payload, delay)
      .then(resp => {
        setCallState(prevState => ({ ...prevState, responseMessage: resp, loading: false }));
      }).catch(error => {
        setCallState(prevState => ({ ...prevState, responseError: error.message, loading: false }));
      });
  }

  const handleUserInput = (e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) => {
    const name = e.target.name;
    const value = e.target.value;
    validateField(name, value)
  }

  const validateField = (fieldName:string, value: any) => {
    let fieldValidationErrors = callState.formErrors;
    let payloadValid = callState.payloadValid;
    let delayValid = callState.delayValid;

    switch (fieldName) {
      case 'payload':
        payloadValid = value.length >= 2;
        fieldValidationErrors.payload = payloadValid ? '' : ' should have atleast 2 characters';
        break;
      case 'delay':
        delayValid = !isNaN(value)
        fieldValidationErrors.delay = delayValid ? '' : ' is not valid';
        break;
      default:
        break;
    }
    setCallState(prevState => ({
      ...prevState,
      [fieldName]:value,
      formErrors: fieldValidationErrors,
      payloadValid: payloadValid,
      delayValid: delayValid
    }));

    validateForm()
  }

  const validateForm = () => {
    setCallState(prevState => ({ ...prevState, formValid: prevState.payloadValid && prevState.delayValid }));
  }
  return (
    <div>
      <div className="row">
        <div className="col-sm-12">
          <h3>Send a request</h3>
          <p className="lead alert alert-info">
            The request will be sent to the appication which will reply after the specified "delay"
          </p>
        </div>
        <div>
          <FormErrors formErrors={callState.formErrors} />
        </div>
      </div>
      <div className="row">
        <div className="col-sm-12">
          <form onSubmit={handleFormSubmit}>
            <div className="form-group row">
              <label htmlFor="payload" className="col-sm-2 col-form-label">Payload</label>
              <div className="col-sm-10">
                <textarea name="payload" className="form-control" placeholder="Payload"
                  onChange={handleUserInput} value={callState.payload}></textarea>
              </div>
            </div>
            <div className="form-group row">
              <label htmlFor="delay" className="col-sm-2 col-form-label">Delay (in ms)</label>
              <div className="col-sm-10">
                <input name="delay" type="number" className="form-control" placeholder="delay"
                  value={callState.delay} onChange={(event) => handleUserInput(event)} />

              </div>
            </div>
            <div className="form-group row">
              <div className="col-sm-10">
                {!callState.loading &&
                  <button name="submit" className="btn btn-primary" disabled={!callState.formValid}>Submit</button>
                }
                {callState.loading &&
                  <button name="submit" className="btn btn-primary disabled"
                    disabled={!callState.formValid}>Submit</button>
                }
              </div>
            </div>
          </form>
        </div>
      </div>
      <ProgressBar loading={callState.loading} />
      <ResponseDisplay responseMessage={callState.responseMessage} responseError={callState.responseError} />
    </div>
  );
}