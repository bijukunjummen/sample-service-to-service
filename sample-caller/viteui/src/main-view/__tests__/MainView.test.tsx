import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { MainForm } from '../MainView'
import * as restCalls from '../client-calls'

// Mock the client-calls module
vi.mock('../client-calls', () => ({
  makePassthroughCall: vi.fn()
}))

describe('MainForm', () => {
  const mockMessageAck = {
    id: '123',
    received: 'test message',
    callerHeaders: new Map(),
    producerHeaders: new Map(),
    callerMetadata: {
      clusterName: 'test-cluster',
      clusterLocation: 'test-location',
      hostName: 'test-host',
      ipAddress: '127.0.0.1',
      region: 'test-region',
      zone: 'test-zone'
    },
    producerMetadata: {
      clusterName: 'test-cluster',
      clusterLocation: 'test-location',
      hostName: 'test-host',
      ipAddress: '127.0.0.1',
      region: 'test-region',
      zone: 'test-zone'
    },
    statusCode: 200,
    roundTripTimeMillis: 100
  }

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders with initial values', () => {
    render(<MainForm payload="test payload" />)
    
    expect(screen.getByLabelText(/payload/i)).toHaveValue('test payload')
    expect(screen.getByLabelText(/delay/i)).toHaveValue(100)
    expect(screen.getByLabelText(/response status code/i)).toHaveValue(200)
    expect(screen.getByRole('button', { name: /submit/i })).toBeEnabled()
  })

  it('validates payload length', async () => {
    render(<MainForm payload="test payload" />)
    
    const payloadInput = screen.getByLabelText(/payload/i)
    await userEvent.clear(payloadInput)
    await userEvent.type(payloadInput, 'a')
    
    expect(await screen.findByText(/should have atleast 2 characters/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /submit/i })).toBeDisabled()
  })

  it('validates delay input', async () => {
    render(<MainForm payload="test payload" />)
    
    const delayInput = screen.getByLabelText(/delay/i)
    await userEvent.clear(delayInput)
    await userEvent.type(delayInput, '-1')
    
    expect(await screen.findByText(/is not valid/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /submit/i })).toBeDisabled()
  })

  it('validates response code input', async () => {
    render(<MainForm payload="test payload" />)
    
    const responseCodeInput = screen.getByLabelText(/response status code/i)
    await userEvent.clear(responseCodeInput)
    await userEvent.type(responseCodeInput, '600')
    
    expect(await screen.findByText(/is not valid/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /submit/i })).toBeDisabled()
  })

  it('submits form successfully', async () => {
    vi.mocked(restCalls.makePassthroughCall).mockResolvedValueOnce(mockMessageAck)
    
    render(<MainForm payload="test payload" />)
    
    const submitButton = screen.getByRole('button', { name: /submit/i })
    await userEvent.click(submitButton)
    
    expect(restCalls.makePassthroughCall).toHaveBeenCalledWith({
      payload: 'test payload',
      delay: 100,
      responseCode: 200
    })
    
    await waitFor(() => {
      expect(screen.getByText(mockMessageAck.id)).toBeInTheDocument()
      expect(screen.getByText(mockMessageAck.received)).toBeInTheDocument()
    })
  })

  it('handles API error', async () => {
    const errorMessage = 'API Error'
    vi.mocked(restCalls.makePassthroughCall).mockRejectedValueOnce(new Error(errorMessage))
    
    render(<MainForm payload="test payload" />)
    
    const submitButton = screen.getByRole('button', { name: /submit/i })
    await userEvent.click(submitButton)
    
    await waitFor(() => {
      expect(screen.getByText(errorMessage)).toBeInTheDocument()
    })
  })

  it('disables submit button while loading', async () => {
    vi.mocked(restCalls.makePassthroughCall).mockImplementationOnce(() => 
      new Promise(resolve => setTimeout(() => resolve(mockMessageAck), 100))
    )
    
    render(<MainForm payload="test payload" />)
    
    const submitButton = screen.getByRole('button', { name: /submit/i })
    await userEvent.click(submitButton)
    
    expect(submitButton).toBeDisabled()
    expect(screen.getByRole('progressbar')).toBeInTheDocument()
    
    await waitFor(() => {
      expect(submitButton).toBeEnabled()
      expect(screen.queryByRole('progressbar')).not.toBeInTheDocument()
    })
  })
})
