import React, { useState } from "react";
import * as restCalls from "./client-calls";
import { MessageAck } from "./models";

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

export const ResponseDisplay = ({ responseMessage, responseError }: { responseMessage?: MessageAck, responseError?: string }) => {
    return <div className="row mt-3">
        <div className="col-sm-12">
            {responseMessage &&
                <div className="alert alert-success">
                    <div className="row">
                        <label htmlFor="id" className="col-sm-2">Id : </label>
                        <span className="col-sm-8">{responseMessage.id}</span>
                    </div>
                    <div className="row">
                        <label htmlFor="message" className="col-sm-2">Recieved : </label>
                        <span className="col-sm-8">{responseMessage.received}</span>
                    </div>
                    <div className="row">
                        <label htmlFor="ack" className="col-sm-2">Caller Headers: </label>
                        <span className="col-sm-8">
                            <div>
                                {responseMessage.callerHeaders &&
                                    Object.entries(responseMessage.callerHeaders).map((value: [string, any], index: number) =>
                                        <div className="row" key={value[0]}>
                                            <label className="col-sm-4">{value[0]}</label><span
                                                className="col-sm-8">{value[1]}</span>
                                        </div>
                                    )}
                            </div>
                        </span>
                    </div>
                    <div className="row">
                        <label htmlFor="ack" className="col-sm-2">Producer Headers: </label>
                        <span className="col-sm-8">
                            <div>
                                {responseMessage.producerHeaders &&
                                    Object.entries(responseMessage.producerHeaders).map((value: [string, any], index: number) =>
                                        <div className="row" key={value[0]}>
                                            <label className="col-sm-4">{value[0]}</label><span
                                                className="col-sm-8">{value[1]}</span>
                                        </div>
                                    )}
                            </div>
                        </span>
                    </div>
                    {responseMessage.callerMetadata &&
                        <div className="row">
                            <label htmlFor="ack" className="col-sm-2">Caller Metadata: </label>
                            <span className="col-sm-8">
                                <div>
                                    <div className="row">
                                        <label className="col-sm-4">Cluster Name</label><span
                                            className="col-sm-8">{responseMessage.callerMetadata.clusterName}</span>
                                    </div>
                                    <div className="row">
                                        <label className="col-sm-4">Cluster Location</label><span
                                            className="col-sm-8">{responseMessage.callerMetadata.clusterLocation}</span>
                                    </div>
                                </div>
                            </span>
                        </div>
                    }
                    {responseMessage.producerMetadata &&
                        <div className="row">
                            <label htmlFor="ack" className="col-sm-2">Producer Metadata: </label>
                            <span className="col-sm-8">
                                <div>
                                    <div className="row">
                                        <label className="col-sm-4">Cluster Name</label><span
                                            className="col-sm-8">{responseMessage.producerMetadata.clusterName}</span>
                                    </div>
                                    <div className="row">
                                        <label className="col-sm-4">Cluster Location</label><span
                                            className="col-sm-8">{responseMessage.producerMetadata.clusterLocation}</span>
                                    </div>
                                </div>
                            </span>
                        </div>
                    }

                    <div className="row">
                        <label htmlFor="statusCode" className="col-sm-2">Status Code : </label>
                        <span className="col-sm-8">{responseMessage.statusCode}</span>
                    </div>
                    <div className="row">
                        <label htmlFor="roundTripTimeMillis" className="col-sm-2">Round Trip Time(millis): </label>
                        <span className="col-sm-8">{responseMessage.roundTripTimeMillis}</span>
                    </div>
                </div>
            }

            {responseError &&
                <div className="alert alert-warning">
                    <div className="row">
                        <span className="col-sm-8">{responseError}</span>
                    </div>
                </div>
            }
        </div>
    </div>
};

interface CallState {
    payload: string;
    delay: number;
    responseCode: number;
    formErrors: Record<string, string>
    loading: boolean;
    formValid: boolean;
    responseMessage?: MessageAck;
    responseError?: string;
}

export const MainForm = ({ payload }: { payload: string }) => {
    const [callState, setCallState] = useState<CallState>({
        payload: "dummy payload",
        delay: 100,
        responseCode: 200,
        formErrors: {},
        loading: false,
        formValid: true,
        responseMessage: undefined,
        responseError: undefined
    });

    const handleFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        if (callState.loading) {
            e.preventDefault();
            return;
        }
        setCallState(prevState => ({ ...prevState, responseError: undefined, responseMessage: undefined }));
        passthroughCallAndSetState(callState.payload, callState.delay, callState.responseCode);
        e.preventDefault()
    }

    const passthroughCallAndSetState = (payload: string, delay: number, responseCode: number = 200) => {
        setCallState(prevState => ({ ...prevState, loading: true }));
        restCalls
            .makePassthroughCall({ payload: payload, delay: delay, responseCode: responseCode })
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

    const validateField = (fieldName: string, value: any) => {
        let fieldValidationErrors = callState.formErrors;
        let payloadValid = true;
        let delayValid = true;

        switch (fieldName) {
            case 'payload':
                payloadValid = value.length >= 2;
                fieldValidationErrors.payload = payloadValid ? '' : ' should have atleast 2 characters';
                break;
            case 'delay':
                delayValid = !isNaN(value) && Number(value) >= 0;
                fieldValidationErrors.delay = delayValid ? '' : ' is not valid';
                break;
            default:
                break;
        }
        setCallState(prevState => ({
            ...prevState,
            [fieldName]: value,
            formErrors: fieldValidationErrors,
            formValid: payloadValid && delayValid
        }));
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
                        <div className="form-group row mb-3">
                            <label htmlFor="payload" className="col-sm-2 col-form-label">Payload</label>
                            <div className="col-sm-10">
                                <textarea name="payload" className="form-control" placeholder="Payload"
                                    onChange={handleUserInput} value={callState.payload}></textarea>
                            </div>
                        </div>
                        <div className="form-group row mb-3">
                            <label htmlFor="delay" className="col-sm-2 col-form-label">Delay (in ms)</label>
                            <div className="col-sm-10">
                                <input name="delay" type="number" className="form-control" placeholder="delay"
                                    value={callState.delay} onChange={handleUserInput} />

                            </div>
                        </div>
                        <div className="form-group row mb-3">
                            <label htmlFor="delay" className="col-sm-2 col-form-label">Response Status Code</label>
                            <div className="col-sm-10">
                                <input name="responseCode" type="number" className="form-control"
                                    placeholder="status code"
                                    value={callState.responseCode} onChange={handleUserInput} />

                            </div>
                        </div>
                        <div className="form-group row mb-3">
                            <div className="col-sm-10">
                                {!callState.loading &&
                                    <button name="submit" className="btn btn-primary"
                                        disabled={!callState.formValid}>Submit</button>
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