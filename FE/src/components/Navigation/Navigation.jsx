import { useState } from "react";
import { AccountIcon } from "../common/AccountIcon";
import { CartIcon } from "../common/CartIcon";
import { Wishlist } from "../common/WishList";

const Navigation = () => {
    const [menuOpen, setMenuOpen] = useState(false);

    return (
        <nav className="bg-white shadow-md fixed top-0 left-0 w-full z-[999]">
            <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                <div className="relative flex h-24 items-center justify-between">
                    {/* Mobile menu button */}
                    <div className="absolute inset-y-0 left-0 flex items-center sm:hidden">
                        <button
                            type="button"
                            onClick={() => setMenuOpen(!menuOpen)}
                            className="inline-flex items-center justify-center rounded-md p-2 text-gray-500 hover:bg-gray-200 hover:text-black focus:outline-none focus:ring-2 focus:ring-inset focus:ring-black"
                            aria-controls="mobile-menu"
                            aria-expanded={menuOpen}
                        >
                            <span className="sr-only">Open main menu</span>
                            {menuOpen ? (
                                <svg
                                    className="block w-6 h-6"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                    stroke="currentColor"
                                    strokeWidth="2"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        d="M6 18L18 6M6 6l12 12"
                                    />
                                </svg>
                            ) : (
                                <svg
                                    className="block w-6 h-6"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                    stroke="currentColor"
                                    strokeWidth="2"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        d="M4 6h16M4 12h16M4 18h16"
                                    />
                                </svg>
                            )}
                        </button>
                    </div>

                    {/* Logo */}
                    <div className="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
                        <a
                            href="/"
                            className="text-3xl font-bold text-[#FF914D]"
                        >
                            ShopNTP
                        </a>
                    </div>

                    {/* Desktop Nav */}
                    <div className="hidden sm:flex justify-center flex-2">
                        <ul className="flex lg:gap-10 md:gap-8 sm:gap-6 text-gray-600 lg:text-lg md:text-md items-center">
                            <li>
                                <a href="/" className="hover:text-[#FF914D]">
                                    Shop
                                </a>
                            </li>
                            <li>
                                <a
                                    href="/mens"
                                    className="hover:text-[#FF914D]"
                                >
                                    Men
                                </a>
                            </li>
                            <li>
                                <a
                                    href="/womens"
                                    className="hover:text-[#FF914D]"
                                >
                                    Women
                                </a>
                            </li>
                            <li>
                                <a
                                    href="/kids"
                                    className="hover:text-[#FF914D]"
                                >
                                    Kids
                                </a>
                            </li>
                        </ul>
                    </div>

                    {/* Search + Icons */}
                    <div className="absolute inset-y-0 right-0 flex items-center gap-3 pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0 flex-2 sm:flex-0">
                        {/* Search */}
                        <div className="hidden md:flex items-center rounded-md overflow-hidden bg-gray-100">
                            <button className="px-2 bg-gray-100">
                                <svg
                                    className="h-4 w-4 text-grey-dark cursor-pointer"
                                    fill="currentColor"
                                    xmlns="http://www.w3.org/2000/svg"
                                    viewBox="0 0 24 24"
                                >
                                    <path d="M16.32 14.9l5.39 5.4a1 1 0 0 1-1.42 1.4l-5.38-5.38a8 8 0 1 1 1.41-1.41zM10 16a6 6 0 1 0 0-12 6 6 0 0 0 0 12z" />
                                </svg>
                            </button>
                            <input
                                type="text"
                                className="px-3 py-2 outline-none text-sm lg:w-60 md:w-40"
                                placeholder="Search"
                            />
                        </div>

                        {/* Icons */}
                        <div className="flex items-center md:gap-3 sm:gap-0.5 lg:ml-10 md:ml-6">
                            <button>
                                <Wishlist />
                            </button>
                            <button>
                                <AccountIcon />
                            </button>
                            <a href="/cart-items">
                                <CartIcon />
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            {/* Mobile menu */}
            {menuOpen && (
                <div className="sm:hidden" id="mobile-menu">
                    <div className="space-y-1 px-2 pt-2 pb-3 text-sm font-medium">
                        <a
                            href="/"
                            className="block px-3 py-2 text-gray-700 hover:bg-gray-100 rounded-md"
                        >
                            Shop
                        </a>
                        <a
                            href="/mens"
                            className="block px-3 py-2 text-gray-700 hover:bg-gray-100 rounded-md"
                        >
                            Men
                        </a>
                        <a
                            href="/womens"
                            className="block px-3 py-2 text-gray-700 hover:bg-gray-100 rounded-md"
                        >
                            Women
                        </a>
                        <a
                            href="/kids"
                            className="block px-3 py-2 text-gray-700 hover:bg-gray-100 rounded-md"
                        >
                            Kids
                        </a>
                    </div>
                </div>
            )}
        </nav>
    );
};

export default Navigation;
