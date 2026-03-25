import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function OauthSuccessPage() {
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const { completeOauthLogin } = useAuth();

  useEffect(() => {
    const token = params.get("token");
    if (!token) {
      navigate("/login");
      return;
    }
    completeOauthLogin(token)
      .then(() => navigate("/dashboard"))
      .catch(() => navigate("/login"));
  }, [completeOauthLogin, navigate, params]);

  return <main className="page"><section className="panel"><p>Signing you in...</p></section></main>;
}

export default OauthSuccessPage;
