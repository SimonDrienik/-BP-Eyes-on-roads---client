<?php

use App\Rola;
use Illuminate\Database\Seeder;

class RolaSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $roly = [
            [
                'nazov' => 'zaregistrovany obcan',
            ],
            [
                'nazov' => 'nezaregistrovany obcan',
            ],
            [
                'nazov' => 'admin',
            ],
            [
                'nazov' => 'dispecer',
            ],
            [
                'nazov' => 'manazer'
            ]
        ];

        foreach ($roly as $rola) {
            Rola::create(array(
                'nazov' => $rola['nazov']
            ));
        }
    }
}
