import { MainForm } from '../main-view/MainView'

export function Home() {
  return (
    <div className="row justify-content-center">
      <div className="col-md-8">
        <MainForm payload="dummy payload" />
      </div>
    </div>
  )
}
