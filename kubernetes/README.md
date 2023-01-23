* Prepare necessary Docker images in your remote repo or local Docker storage.

* Install Kubernetes to your computer.

* Install Ingress Nginx project by command:
`kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.5.1/deploy/static/provider/cloud/deploy.yaml`

* Write any secret env variables (like: MONGO_DB_PASSWORD , EMAIL_SENDER_API_TOKEN , e.t.c.) as a **Secret** object to your local Kubernetes by command:
`kubectl create secret generic [secret_name] --from-literal [key]=[value]`

* Feed all files in dir **'k8s'** for **kubctl** by running command:
`kubectl apply -f k8s`

* Run requests without port value in order to test locally.