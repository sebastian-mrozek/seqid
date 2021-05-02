<script lang="ts">
  import type { NumericSequenceDefinition } from "../model";
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  let namespace = "";
  let name = "";
  let start = 1;

  function onClick() {
    dispatch("create", { name, namespace, start });
    console.log(start);
  }

  $: valid = namespace.length > 0 && name.length > 0 && start !== undefined && start !== null;
</script>

<div class="wrapper">
  <div class="labeled-edit">
    <label for="name">Name</label>
    <input id="name" type="text" bind:value={name} />
  </div>
  <div class="labeled-edit">
    <label for="namespace">Namespace</label>
    <input id="namespace" type="text" bind:value={namespace} />
  </div>
  <div class="labeled-edit">
    <label for="start">Start</label>
    <input id="start" type="number" bind:value={start} />
  </div>
  <button class="add" on:click={onClick} disabled={!valid}>Add</button>
</div>

<style>
  .add {
    align-self: center;
    padding: 1em;
  }
  .labeled-edit {
    display: flex;
    flex-direction: column;
    margin: 1em;
  }
  label {
    font-variant: small-caps;
    color: #555;
  }
  input {
    border-radius: 3px;
    border: none;
    padding: 0.5em;
    font-size: 1.2em;
  }
  .wrapper {
    display: flex;
    margin-top: 1em;
    background-color: #eee;
    border-radius: 3px;
  }
</style>
