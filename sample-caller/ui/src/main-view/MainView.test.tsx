import { render, screen } from '@testing-library/react';
import React from 'react';
import renderer from 'react-test-renderer';
import {MainForm} from './MainView';

describe("Main View of Client UI", () => {

  it('renders main view with Payload and Delay Form', () => {
    render(<MainForm payload="test"/>);

    const payloadElement = screen.getByText("Payload");
    expect(payloadElement).toBeInTheDocument();
    expect(screen.getByText(/Delay/)).toBeInTheDocument();
  });

  // it("handles form submissions", () => {
  //   const wrapper = render(<MainForm payload="test"/>);
  //   const passthroughCallSpy = jest.spyOn(
  //     wrapper,
  //     'passthroughCallAndSetState'
  //   ).mockImplementation((payload, delay) => {
  //   });
  //   expect(wrapper.find("button[name='submit']").html()).toMatch(/Submit/)

  //   wrapper.find("button[name='submit']").simulate("submit");
  //   expect(passthroughCallSpy).toHaveBeenCalledWith("Sample", 100);
  //   expect(wrapper.state("loading")).toBe(false)
  // });

  // it("handles form validations", () => {
  //   const wrapper = mount(<MainForm />);
  //   wrapper.find("textarea[name='payload']").simulate('change', {target: {name: 'payload', value: 'S'}});
  //   expect(wrapper.state("payloadValid")).toBe(false)
  // });
});

