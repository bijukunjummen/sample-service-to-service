export function About() {
  return (
    <div className="row justify-content-center">
      <div className="col-md-8">
        <div className="card">
          <div className="card-body">
            <h2 className="card-title">About Service Caller</h2>
            <p className="card-text">
              This application demonstrates service-to-service communication in a modern web application.
              Built with React, TypeScript, and Bootstrap, it showcases how to make API calls to backend services
              and handle responses effectively.
            </p>
            <h5 className="mt-4">Technologies Used:</h5>
            <ul className="list-group list-group-flush">
              <li className="list-group-item">React with TypeScript</li>
              <li className="list-group-item">Bootstrap for UI components</li>
              <li className="list-group-item">React Router for navigation</li>
              <li className="list-group-item">Vite for build tooling</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  )
}
