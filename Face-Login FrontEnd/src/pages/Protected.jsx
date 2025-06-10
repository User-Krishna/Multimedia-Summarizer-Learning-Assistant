import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Protected() {
  const [account, setAccount] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const faceAuth = localStorage.getItem("faceAuth");
    if (!faceAuth) {
      navigate("/login");
    } else {
      const { account } = JSON.parse(faceAuth);
      setAccount(account);
    }
  }, [navigate]);

  if (!account) {
    return null;
  }

  return (
    <div
      className="bg-white pt-40 md:pt-60"
      style={{
        backgroundImage: "url('/src/pages/log.jpg')", // Path to your background image
        backgroundSize: "cover",
        backgroundPosition: "center",
        minHeight: "100vh",  // Ensure it covers the full viewport height
      }}
    >
      <div className="mx-auto max-w-7xl h-full flex flex-col justify-center items-center">
        {/* Adjusting the header to move it up a bit */}
        <h2 className="text-center text-3xl font-extrabold tracking-tight text-gray-900 sm:text-4xl mb-6">
          You have successfully logged in!
        </h2>
        
        <div className="text-center mb-24 relative">
          {/* Image with rotating border */}
          <div className="relative inline-block mb-8">
            <div className="absolute inset-0 border-8 border-t-red-500 border-b-yellow-500 rounded-full animate-spin-slow"></div>
            <img
              className="mx-auto object-cover h-72 w-72 rounded-full border-4 border-white"
              src={account?.type === "CUSTOM" ? account.picture : `${account.picture}`}
              alt={account.fullName}
            />
          </div>

          <h1
            className="block text-4xl tracking-tight font-extrabold bg-clip-text text-transparent bg-gradient-to-r from-blue-500 to-green-500"
            style={{
              lineHeight: "1.5",
            }}
          >
            {account?.fullName}
          </h1>

          <div
            onClick={() => {
              localStorage.removeItem("faceAuth"); // Remove authentication details
              window.location.href = "http://localhost:8088/Major-Project/landing.html"; // Redirect to landing.html
            }}
            className="flex gap-2 mt-12 w-fit mx-auto cursor-pointer z-10 py-3 px-6 rounded-full bg-gradient-to-r from-purple-400 to-purple-600 hover:from-purple-600 hover:to-yellow-500 transition-all"
          >
            <span className="text-white">Get Back to Work</span>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth={1.5}
              stroke="white"
              className="w-6 h-6"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15m3 0l3-3m0 0l-3-3m3 3H9"
              />
            </svg>
          </div>
        </div>
      </div>

      {/* Add the CSS directly within the component */}
      <style jsx>{`
        /* Add the following CSS to rotate the border */
        @keyframes rotate-border {
          0% {
            transform: rotate(0deg);
          }
          50% {
            transform: rotate(180deg); /* Rotate half way */
          }
          100% {
            transform: rotate(360deg); /* Rotate full way */
          }
        }

        /* Class for rotating the border with transition */
        .animate-spin-slow {
          animation: rotate-border 3s ease-in-out infinite; /* Slow down the rotation and add easing */
          transition: transform 0.5s ease-in-out; /* Smooth transition effect */
        }
      `}</style>
    </div>
  );
}

export default Protected;