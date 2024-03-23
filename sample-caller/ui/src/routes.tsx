import {createBrowserRouter, RouterProvider} from 'react-router-dom';
import App from "./App";
import {MainForm} from "./main-view/MainView";
import {Help} from "./help/Help"

const router = createBrowserRouter([
    {
        path: '/',
        element: <App/>,
        children: [
            {
                path: "",
                element: <MainForm payload={`dummy payload`}/>
            },
            {
                path: '/help',
                element: <Help/>
            }

        ]
    },
]);

export function Routes() {
    return <RouterProvider router={router}/>;
}